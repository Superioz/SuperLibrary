package de.superioz.library.bukkit.common;

import de.superioz.library.bukkit.common.protocol.EnumWrappers;
import de.superioz.library.bukkit.common.protocol.PacketType;
import de.superioz.library.bukkit.common.protocol.WrappedChatComponent;
import de.superioz.library.bukkit.common.protocol.WrappedPacket;
import de.superioz.library.bukkit.util.ChatUtil;
import de.superioz.library.java.util.time.TimeUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Class created on April in 2015
 */
public class ViewManager {

	/**
	 * Send title to given players
	 *
	 * @param title           The title
	 * @param subtitle        The subtitle
	 * @param fadeIn          Fadein time
	 * @param stay            Stay time
	 * @param fadeOut         Fadeout time
	 * @param timeUnitSeconds Seconds or ticks?
	 * @param players         The players
	 */
	public static void sendTitle(String title, String subtitle, int fadeIn,
	                             int stay, int fadeOut, boolean timeUnitSeconds, Player... players){
		new CraftTitle(title, subtitle, new int[]{fadeIn, stay, fadeOut}).send(timeUnitSeconds, players);
	}

	/**
	 * Send title to given players
	 *
	 * @param title    The title
	 * @param subtitle The subtitle
	 * @param players  The players
	 */
	public static void sendTitle(String title, String subtitle, Player... players){
		sendTitle(title, subtitle, 1, 1, 1, true, players);
	}

	/**
	 * Send title to given players
	 *
	 * @param title   The title
	 * @param players The players
	 */
	public static void sendTitle(String title, Player... players){
		sendTitle(title, "", players);
	}

	/**
	 * Set sight for given spectator
	 *
	 * @param camera    The camera
	 * @param spectator The spectator who gets given camera
	 */
	public static void setSight(Player camera, Player spectator){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.CAMERA);
		packet.getIntegers().write(0, camera.getEntityId());

		packet.send(spectator);
	}

	/**
	 * Send hot bar message to given player
	 *
	 * @param text   The text
	 * @param player The player
	 */
	public static void sendHotbarMessage(String text, Player... player){
		byte target = (byte) 2;

		// Get protocol
		WrappedPacket wrappedPacket = new WrappedPacket(PacketType.Play.Server.CHAT);
		wrappedPacket.getChatComponents().write(0, new WrappedChatComponent(ChatUtil.colored(text)));
		wrappedPacket.getBytes().write(0, target);

		wrappedPacket.send(player);
	}

	/**
	 * Send bossbar to given players with given values
	 *
	 * @param title    The title
	 * @param uuid     The id
	 * @param color    The color
	 * @param style    The style
	 * @param progress The progress (0-1)
	 * @param action   The action
	 * @param players  The viewers
	 */
	public static void sendBossbar(String title, UUID uuid, EnumWrappers.BossbarColor color,
	                               EnumWrappers.BossbarStyle style, float progress,
	                               EnumWrappers.BossbarAction action, Player... players){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.BOSS);

		packet.getUniqueIdentifier().write(0, uuid);
		packet.getBossbarActionModifier().write(0, action);
		packet.getChatComponents().write(0, new WrappedChatComponent(ChatUtil.colored(title)));
		packet.getFloats().write(0, progress);
		packet.getBossbarColorModifier().write(0, color);
		packet.getBossbarStyleModifier().write(0, style);

		packet.send(players);
	}

	/**
	 * Class created on April in 2015
	 */
	private static class CraftTitle {

		protected String title;
		protected String subTitle;
		protected int[] timings;

		public CraftTitle(String title, String subTitle, int[] timings){
			this.title = title;
			this.subTitle = subTitle;
			this.setTimings(timings);
		}

		/**
		 * Set the timings for this
		 *
		 * @param timings The timings as array
		 */
		public void setTimings(int[] timings){
			int[] timeValues = timings;

			if(timings != null && timings.length != 3){
				timeValues = new int[]{1, 1, 1};
			}

			this.timings = timeValues;
		}

		/**
		 * Get the protocol
		 *
		 * @return The container
		 */
		public WrappedPacket getPacket(){
			return new WrappedPacket(PacketType.Play.Server.TITLE);
		}

		/**
		 * Send protocol to given players
		 *
		 * @param ticks   The ticks
		 * @param players The players
		 */
		public void send(boolean ticks, Player... players){
			// Timings
			if(timings != null && timings.length == 3){
				WrappedPacket timingsPacket = getPacket();

				// Title
				timingsPacket.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
				timingsPacket.getIntegers().write(0, TimeUtils.convertTime(timings[0], ticks));
				timingsPacket.getIntegers().write(1, TimeUtils.convertTime(timings[1], ticks));
				timingsPacket.getIntegers().write(2, TimeUtils.convertTime(timings[2], ticks));


				// Send
				timingsPacket.send(players);
			}

			// Title
			if(title != null && !title.isEmpty()){
				WrappedPacket titlePacket = getPacket();

				// Data
				titlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
				titlePacket.getChatComponents().write(0, new WrappedChatComponent(ChatUtil.colored(title)));

				// Send
				titlePacket.send(players);
			}

			// Subtitle
			if(subTitle != null && !subTitle.isEmpty()){
				WrappedPacket subtitlePacket = getPacket();

				// Title
				subtitlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
				subtitlePacket.getChatComponents().write(0, new WrappedChatComponent(ChatUtil.colored(subTitle)));

				// Send
				subtitlePacket.send(players);
			}
		}

	}
}
