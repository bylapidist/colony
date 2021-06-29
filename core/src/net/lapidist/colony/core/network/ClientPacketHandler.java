package net.lapidist.colony.core.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.DatagramPacket;

public class ClientPacketHandler extends
        SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected final void channelRead0(
            final ChannelHandlerContext ctx,
            final DatagramPacket msg
    ) {
        byte[] bytes = msg.getData();
        System.out.println("Received packet, length: " + bytes.length);
    }

    @Override
    public final void exceptionCaught(
            final ChannelHandlerContext ctx,
            final Throwable cause
    ) throws Exception {
        System.err.println(cause.getMessage());
        ctx.close();
        throw new Exception(cause);
    }
}
