package de.superioz.library.bukkit.common.protocol;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * Created on 03.04.2016.
 */
@Getter
public class PacketEvent {

	private WrappedPacket packet;
	private Player player;

	@Setter
	private boolean cancelled;

	public PacketEvent(WrappedPacket packet, Player player){
		this.packet = packet;
		this.player = player;
	}

	public PacketType getType(){
		return getPacket().getType();
	}

}
