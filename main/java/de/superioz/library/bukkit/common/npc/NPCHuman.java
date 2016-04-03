package de.superioz.library.bukkit.common.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.superioz.library.bukkit.common.protocol.*;
import de.superioz.library.bukkit.common.session.GameProfileBuilder;
import de.superioz.library.bukkit.common.session.UUIDFetcher;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created on 01.04.2016.
 */
@Getter
public class NPCHuman extends NPC {

	protected Profile profile;
	protected UUID uuid;
	protected GameProfile gameProfile;
	protected Collection<Property> playerSkin;

	protected Thread fetchingThread;
	protected NPCHumanInventory inventory;

	/**
	 * Default constructor for npc human
	 *
	 * @param location    The location where to spawn first
	 * @param displayName The displayname
	 * @param fixedAim    Should he looks after players?
	 */
	public NPCHuman(Location location, String displayName, boolean fixedAim, Profile profile){
		super(location, displayName, fixedAim);
		this.profile = profile;
		this.inventory = new NPCHumanInventory();
		this.setMetadata(ProtocolEntityMeta.PLAYER_ADDITIONAL_HEARTS, 1F);

		this.fetchingThread = new Thread(new Runnable() {
			@Override
			public void run(){
				gameProfile = initProfile();
				playerSkin = gameProfile.getProperties().get(getProfile().getSkinName());
				uuid = gameProfile.getId();
			}
		});
		this.fetchingThread.start();
	}

	public NPCHuman(Location location, String displayName, Profile profile){
		this(location, displayName, false, profile);
	}

	/**
	 * Initialises the game profile
	 *
	 * @return The game profile
	 */
	private GameProfile initProfile(){
		GameProfile profile;
		try{
			profile = GameProfileBuilder.fetch(UUIDFetcher.getUUIDOf(getProfile().getSkinName()));
			Field name = profile.getClass().getDeclaredField("name");
			name.setAccessible(true);
			name.set(profile, getProfile().getSkinName());
		}
		catch(Exception e){
			profile = new GameProfile(UUID.randomUUID(), getDisplayName());
		}
		return profile;
	}

	/**
	 * Add this fake player to tablist
	 */
	public void addToTablist(Player... players){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.PLAYER_INFO);

		packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
		packet.getPlayerInfoDataLists().write(0, Collections.singletonList(new PlayerInfoData(EnumWrappers.NativeGameMode.NOT_SET,
				getGameProfile(), 0, new WrappedChatComponent(getDisplayName()))));
		packet.send(players);
	}

	/**
	 * Remove this fakeplayer from tablist
	 */
	public void removeFromTablist(){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.PLAYER_INFO);

		packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
		packet.getPlayerInfoDataLists().write(0, Collections.singletonList(new PlayerInfoData(EnumWrappers.NativeGameMode.NOT_SET,
				getGameProfile(), 0, new WrappedChatComponent(getDisplayName()))));
		packet.send(getViewerAsPlayer());
	}

	/**
	 * Update inventory content
	 */
	public void updateInventory(){
		List<WrappedPacket> packets = this.inventory.createPackets(getEntityId());
		if(packets.isEmpty()) return;

		for(WrappedPacket packet : packets){
			packet.send(getViewerAsPlayer());
		}
	}

	@Override
	public void spawn(Player... players){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);

		// First add player to tablist
		// Wait for finishing thread
		try{
			this.fetchingThread.join();
		}catch(InterruptedException e){ e.printStackTrace(); }
		this.addToTablist(players);

		packet.getIntegers().write(0, getEntityId());
		packet.getUniqueIdentifier().write(0, getGameProfile().getId());
		packet.getDoubles().write(0, getLocation().getX());
		packet.getDoubles().write(1, getLocation().getY());
		packet.getDoubles().write(2, getLocation().getZ());
		packet.getDataWatcherModifier().write(0, getDataWatcher());
		packet.send(players);

		// Other
		this.updateInventory();

		// Set viewer
		super.setViewers(true, players);
		super.register();
	}

	@Getter
	public static class Profile {

		protected String skinName;
		protected String capeUrl;

		public Profile(String capeUrl, String skinName){
			this.capeUrl = capeUrl;
			this.skinName = skinName;
		}

	}

}
