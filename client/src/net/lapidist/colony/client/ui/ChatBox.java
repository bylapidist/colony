package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.i18n.I18n;

/**
 * Simple chat UI widget containing a log and input field.
 */
public final class ChatBox extends Table {
    private static final int PREF_ROWS = 6;
    private static final float LOG_HEIGHT = 100f;
    private final TextArea log;
    private final TextField input;
    private final GameClient client;

    public ChatBox(final Skin skin, final GameClient clientToUse) {
        this.client = clientToUse;
        log = new TextArea("", skin);
        log.setDisabled(true);
        log.setPrefRows(PREF_ROWS);
        input = new TextField("", skin);
        input.setMessageText(I18n.get("chat.placeholder"));
        input.addListener(new InputListener() {
            @Override
            public boolean keyTyped(final InputEvent event, final char character) {
                if (character == '\n' || character == '\r') {
                    submit();
                    return true;
                }
                return false;
            }
        });
        add(log).growX().height(LOG_HEIGHT).row();
        add(input).growX();
    }

    private void submit() {
        String text = input.getText().trim();
        if (!text.isEmpty()) {
            client.sendChatMessage(new ChatMessage(text));
            input.setText("");
        }
    }

    public void addMessage(final ChatMessage msg) {
        log.appendText(msg.text() + "\n");
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        ChatMessage msg;
        while ((msg = client.pollChatMessage()) != null) {
            addMessage(msg);
        }
    }
}
