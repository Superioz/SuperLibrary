package de.superioz.library.bukkit.common.protocol;

import org.bukkit.entity.Player;

/**
 * Created on 31.03.2016.
 */
public interface PacketHandler {

	/**
	 * Listener when a packet comes in (INPUT)
	 *
	 * @param event The event
	 */
	void onReceive(PacketEvent event);

	/**
	 * Listener when a packet comes out (OUTPUT)
	 *
	 * @param event The event
	 */
	void onSend(PacketEvent event);

}
