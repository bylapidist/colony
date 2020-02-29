package net.lapidist.colony.core.codecs.encoders;

import com.badlogic.gdx.ai.msg.Telegram;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.lapidist.colony.core.packets.TelegramPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TelegramPacketEncoder extends AbstractPacketEncoder {

    public TelegramPacketEncoder(Telegram telegram) {
        setPacket(encode(new TelegramPacket(telegram)));
    }

    protected ByteBuf encode(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os;
        try {
             os = new ObjectOutputStream(out);
            os.writeObject(obj);
            } catch (IOException e) {
           e.printStackTrace();
           }
           return Unpooled.copiedBuffer(out.toByteArray());
     }
}
