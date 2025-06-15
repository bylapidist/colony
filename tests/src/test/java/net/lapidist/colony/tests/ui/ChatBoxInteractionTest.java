package net.lapidist.colony.tests.ui;

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

@RunWith(GdxTestRunner.class)
public class ChatBoxInteractionTest {

    @Test
    public void submitsMessageWithPlayerIdAndHidesInput() throws Exception {
        final int playerId = 7;
        GameClient client = mock(GameClient.class);
        when(client.getPlayerId()).thenReturn(playerId);
        Skin skin = new Skin(com.badlogic.gdx.Gdx.files.internal("skin/default.json"));
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        ChatBox box = new ChatBox(skin, client);
        stage.addActor(box);

        box.showInput();
        Field inputField = ChatBox.class.getDeclaredField("input");
        inputField.setAccessible(true);
        com.badlogic.gdx.scenes.scene2d.ui.TextField input =
                (com.badlogic.gdx.scenes.scene2d.ui.TextField) inputField.get(box);
        input.setText("hello");
        stage.keyTyped('\n');

        verify(client).sendChatMessage(new ChatMessage(playerId, "hello"));
        assertFalse(box.isInputVisible());
        assertEquals("", input.getText());
    }
}
