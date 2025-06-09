package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.i18n.I18n;

/**
 * Simple chat UI widget containing a log and input field.
 */
public final class ChatBox extends Table {
    private static final int PREF_ROWS = 6;
    private static final float LOG_HEIGHT = 100f;
    private static final float PADDING = 10f;
    private final TextArea log;
    private final TextField input;
    private final GameClient client;
    private final Skin skin;
    private Dialog dialog;

    public ChatBox(final Skin skinToUse, final GameClient clientToUse) {
        this.client = clientToUse;
        this.skin = skinToUse;
        log = new TextArea("", skinToUse);
        log.setDisabled(true);
        log.setPrefRows(PREF_ROWS);
        input = new TextField("", skinToUse);
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
    }

    private void submit() {
        String text = input.getText().trim();
        if (!text.isEmpty()) {
            client.sendChatMessage(new ChatMessage(client.getPlayerId(), text));
            input.setText("");
        }
        hideInput();
    }

    public void showInput() {
        if (dialog != null) {
            return;
        }
        dialog = new Dialog(I18n.get("chat.dialogTitle"), skin, "dialog") {
            @Override
            protected void result(final Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    submit();
                } else {
                    hideInput();
                }
            }
        };
        dialog.getContentTable().add(input).growX().pad(PADDING);
        dialog.button(I18n.get("chat.send"), true);
        dialog.button(I18n.get("common.cancel"), false);
        dialog.show(getStage());
        getStage().setKeyboardFocus(input);
    }

    public void hideInput() {
        if (dialog != null) {
            dialog.hide();
            dialog = null;
        }
        getStage().unfocus(input);
    }

    public boolean isInputVisible() {
        return dialog != null && dialog.getStage() != null;
    }

    public void addMessage(final ChatMessage msg) {
        String format = I18n.get("chat.format");
        String line = String.format(format, msg.playerId(), msg.text());
        log.appendText(line + "\n");
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
