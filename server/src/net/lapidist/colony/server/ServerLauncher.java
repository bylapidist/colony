package net.lapidist.colony.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.codecs.decoders.PacketDecoder;

import java.net.InetAddress;

public final class ServerLauncher {

    private ServerLauncher() {
    }

    public static void main(final String[] arg) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_RCVBUF, Constants.MAX_PACKET_SIZE)
                    .option(
                            ChannelOption.RCVBUF_ALLOCATOR,
                            new FixedRecvByteBufAllocator(
                                    Constants.MAX_PACKET_SIZE
                            )
                    )
                    .localAddress(Constants.DEFAULT_PORT)
                    .handler(channelInitializer());

            String address = InetAddress.getLocalHost().getHostAddress();

            // Start the server.
            ChannelFuture f = b.bind().sync();

            System.out.printf(
                    "Server started!\nListening on: %s:%d\n",
                    address,
                    Constants.DEFAULT_PORT
            );

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            group.shutdownGracefully();

            // Wait until all threads are terminated.
            group.terminationFuture().sync();
        }
    }

    private static ChannelInitializer<NioDatagramChannel> channelInitializer() {
        return new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new PacketDecoder());
            }
        };
    }
}
