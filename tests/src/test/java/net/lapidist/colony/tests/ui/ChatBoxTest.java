package net.lapidist.colony.tests.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.network.ChatMessage;
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
        when(client.poll(ChatMessage.class)).thenReturn(new ChatMessage(1, "hi"), null);

        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        ChatBox box = new ChatBox(skin, client);

        box.act(0f);

        Field logField = ChatBox.class.getDeclaredField("log");
        logField.setAccessible(true);
        com.badlogic.gdx.scenes.scene2d.ui.TextArea log =
                (com.badlogic.gdx.scenes.scene2d.ui.TextArea) logField.get(box);
        assertTrue(log.getText().contains("1: hi"));
    }

    @Test
    public void keepsMaxMessagesAndScrollsToBottom() throws Exception {
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        ChatBox box = new ChatBox(skin, mock(GameClient.class));
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        stage.addActor(box);
        final int size = 200;
        stage.getViewport().update(size, size, true);

        Field maxField = ChatBox.class.getDeclaredField("MAX_MESSAGES");
        maxField.setAccessible(true);
        int max = maxField.getInt(null);

        final int extra = 5;
        for (int i = 0; i < max + extra; i++) {
            box.addMessage(new ChatMessage(i, "msg" + i));
        }

        Field logField = ChatBox.class.getDeclaredField("log");
        logField.setAccessible(true);
        com.badlogic.gdx.scenes.scene2d.ui.TextArea log =
                (com.badlogic.gdx.scenes.scene2d.ui.TextArea) logField.get(box);
        String[] lines = log.getText().split("\\n");
        assertEquals(max, lines.length);
        assertFalse(lines[0].startsWith("0:"));

        stage.act(0f);

        Field scrollField = ChatBox.class.getDeclaredField("scroll");
        scrollField.setAccessible(true);
        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scroll =
                (com.badlogic.gdx.scenes.scene2d.ui.ScrollPane) scrollField.get(box);
        assertEquals(scroll.getMaxY(), scroll.getScrollY(), 0f);
    }
}
