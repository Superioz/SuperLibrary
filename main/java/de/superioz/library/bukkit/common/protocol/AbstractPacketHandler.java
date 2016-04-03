package de.superioz.library.bukkit.common.protocol;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import org.bukkit.entity.Player;


/**
 * Created on 31.03.2016.
 */
@Getter
public class AbstractPacketHandler extends ChannelDuplexHandler {

	private PacketHandler handler;
	private Player player;

	public AbstractPacketHandler(Player player, PacketHandler handler){
		this.handler = handler;
		this.player = player;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		PacketEvent event = new PacketEvent(new WrappedPacket(msg), player);
		this.handler.onSend(event);

		if(!event.isCancelled())
			super.write(ctx, msg, promise);
	}

	@Override
	public void channelRead(ChannelHandlerContext c, Object msg) throws Exception{
		PacketEvent event = new PacketEvent(new WrappedPacket(msg), player);
		this.handler.onReceive(event);

		if(!event.isCancelled())
			super.channelRead(c, msg);
	}
}
