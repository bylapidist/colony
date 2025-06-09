package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.i18n.I18n;

/**
 * Dialog used for sending chat messages.
 */
public final class ChatDialog extends Dialog {
    private static final float PADDING = 10f;
    private static final float WIDTH = 200f;
    private final TextField input;
    private final GameClient client;

    public ChatDialog(final Skin skin, final GameClient clientToUse) {
        super(I18n.get("chat.dialogTitle"), skin, "dialog");
        this.client = clientToUse;
        input = new TextField("", skin);
        input.setMessageText(I18n.get("chat.placeholder"));
        getContentTable().add(input).pad(PADDING).width(WIDTH);
        TextButton send = new TextButton(I18n.get("chat.send"), skin);
        button(send, true);
        key(com.badlogic.gdx.Input.Keys.ENTER, true);
        input.addListener(new InputListener() {
            @Override
            public boolean keyTyped(final InputEvent event, final char character) {
                if (character == '\n' || character == '\r') {
                    result(true);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void result(final Object obj) {
        if (Boolean.TRUE.equals(obj)) {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                client.sendChatMessage(new ChatMessage(client.getPlayerId(), text));
            }
        }
        hide();
    }
}
