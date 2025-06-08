package net.lapidist.colony.tests.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.ui.ChatBox;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** Tests for {@link ChatBox}. */
@RunWith(GdxTestRunner.class)
public class ChatBoxTest {

    @Test
    public void pollsClientMessagesIntoLog() throws Exception {
        GameClient client = mock(GameClient.class);
        when(client.pollChatMessage()).thenReturn(new ChatMessage("hi"), null);

        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        ChatBox box = new ChatBox(skin, client);

        box.act(0f);

        Field logField = ChatBox.class.getDeclaredField("log");
        logField.setAccessible(true);
        com.badlogic.gdx.scenes.scene2d.ui.TextArea log =
                (com.badlogic.gdx.scenes.scene2d.ui.TextArea) logField.get(box);
        assertTrue(log.getText().contains("hi"));
    }
}
