package net.lapidist.colony.core.network;

import com.badlogic.gdx.ai.msg.Telegram;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.lapidist.colony.core.codecs.encoders.TelegramPacketEncoder;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.IListener;

import java.net.InetSocketAddress;

public class Client implements IListener {

    public enum ClientState {
        DISCONNECTED, CONNECTED
    }

    private ClientState state = ClientState.DISCONNECTED;
    private Channel channel;
    private InetSocketAddress remoteAddress;

    public Client(
            final String host,
            final int port
    ) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(channelInitializer());

            addMessageListeners();

            this.remoteAddress = new InetSocketAddress(host, port);
            this.channel = b.bind(0).sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private ChannelInitializer<NioDatagramChannel> channelInitializer() {
        return new ChannelInitializer<>() {
            @Override
            public void initChannel(final NioDatagramChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new ClientPacketHandler());
            }
        };
    }

    @Override
    public final void addMessageListeners() {
        Events.getInstance().addListener(
                this,
                Events.EventType.GAME_INIT.getMessage()
        );
        Events.getInstance().addListener(
                this,
                Events.EventType.PAUSE.getMessage()
        );
        Events.getInstance().addListener(
                this,
                Events.EventType.RESUME.getMessage()
        );
    }

    @Override
    public final boolean handleMessage(final Telegram msg) {
        TelegramPacketEncoder packet = new TelegramPacketEncoder(msg);

        if (
                packet.getPacket().readableBytes() > 0
                        && this.channel != null
                        && this.remoteAddress != null
        ) {
            this.channel.writeAndFlush(
                    new DatagramPacket(packet.getPacket(), this.remoteAddress)
            );

            System.out.printf("[Client] Sent event: %s\n", msg.extraInfo);
            return true;
        }

        return false;
    }

    public final ClientState getState() {
        return state;
    }

    public final void setState(final ClientState stateToSet) {
        this.state = stateToSet;
    }

    public final boolean isConnected() {
        return this.channel.isActive();
    }
}
