package de.superioz.library.bukkit.common.protocol;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created on 31.03.2016.
 */
public class PacketInjector implements Listener {

	private static final String PACKET_HANDLER = "packet_handler";
	private static final String PACKET_INJECTOR = "PacketInjector";

	private static List<PacketHandler> packetListenerRegistry = new ArrayList<>();

	/**
	 * The default packet handler
	 */
	private static final PacketHandler DEFAULT_PACKET_HANDLER = new PacketHandler() {
		@Override
		public void onReceive(final PacketEvent event){
			for(final PacketHandler ph : packetListenerRegistry){
				Executors.newSingleThreadExecutor().submit(new Runnable() {
					@Override
					public void run(){
						ph.onReceive(event);
					}
				});
			}
		}

		@Override
		public void onSend(final PacketEvent event){
			for(final PacketHandler ph : packetListenerRegistry){
				Executors.newSingleThreadExecutor().submit(new Runnable() {
					@Override
					public void run(){
						ph.onSend(event);
					}
				});
			}
		}
	};

	/**
	 * Adds given packet handler to list
	 *
	 * @param handler The handler
	 */
	public static void addPacketHandler(PacketHandler handler){
		if(!packetListenerRegistry.contains(handler))
			packetListenerRegistry.add(handler);
	}

	/**
	 * Removes given packet handler from list
	 *
	 * @param handler The handler
	 */
	public static void removePacketHandler(PacketHandler handler){
		if(packetListenerRegistry.contains(handler))
			packetListenerRegistry.remove(handler);
	}

	/**
	 * Adds packet channeLListener to given player
	 *
	 * @param p The player
	 */
	private static void inject(Player p){
		try{
			Channel ch = ProtocolUtil.getChannel(ProtocolUtil.getNetworkManager(p));
			if(ch.pipeline().get(PACKET_INJECTOR) == null){
				AbstractPacketHandler h = new AbstractPacketHandler(p, DEFAULT_PACKET_HANDLER);
				ch.pipeline().addBefore(PACKET_HANDLER, PACKET_INJECTOR, h);
			}
		}
		catch(Throwable t){
			throw new RuntimeException("Couldn't inject packet handler. (" + t.getMessage() + ")");
		}
	}

	/**
	 * Removes packet channelListener from given player
	 *
	 * @param p The player
	 */
	private static void remove(Player p){
		try{
			Channel ch = ProtocolUtil.getChannel(ProtocolUtil.getNetworkManager(p));
			if(ch.pipeline().get(PACKET_INJECTOR) != null){
				ch.pipeline().remove(PACKET_INJECTOR);
			}
		}
		catch(Throwable t){
			throw new RuntimeException("Couldn't remove packet handler. (" + t.getMessage() + ")");
		}
	}

	/**
	 * Event when a player joins
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();

		if(packetListenerRegistry.size() > 0)
			PacketInjector.inject(player);
	}

}
