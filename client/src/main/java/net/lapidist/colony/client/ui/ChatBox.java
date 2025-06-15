package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import net.lapidist.colony.network.ChatMessage;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.util.I18n;

/**
 * Simple chat UI widget containing a log and input field.
 */
public final class ChatBox extends Table {
    private static final int PREF_ROWS = 6;
    private static final float LOG_HEIGHT = 100f;
    private static final int MAX_MESSAGES = 100;
    private final TextArea log;
    private final ScrollPane scroll;
    private final TextField input;
    private final GameClient client;
    private final java.util.Deque<String> messages = new java.util.ArrayDeque<>();

    public ChatBox(final Skin skin, final GameClient clientToUse) {
        this.client = clientToUse;
        log = new TextArea("", skin);
        log.setDisabled(true);
        log.setPrefRows(PREF_ROWS);
        // Remove background graphics to avoid drawing a border line
        TextField.TextFieldStyle logStyle = new TextField.TextFieldStyle(log.getStyle());
        logStyle.background = null;
        logStyle.disabledBackground = null;
        logStyle.focusedBackground = null;
        log.setStyle(logStyle);
        scroll = new ScrollPane(log, skin);
        scroll.setScrollingDisabled(true, false);
        scroll.setFadeScrollBars(false);
        input = new TextField("", skin);
        input.setVisible(false);
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
        add(scroll).growX().height(LOG_HEIGHT).row();
        add(input).growX();
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
        input.setVisible(true);
        getStage().setKeyboardFocus(input);
    }

    public void hideInput() {
        input.setVisible(false);
        getStage().unfocus(input);
    }

    public boolean isInputVisible() {
        return input.isVisible();
    }

    public void addMessage(final ChatMessage msg) {
        String format = I18n.get("chat.format");
        String line = String.format(format, msg.playerId(), msg.text());
        messages.addLast(line);
        while (messages.size() > MAX_MESSAGES) {
            messages.removeFirst();
        }
        log.setText(String.join("\n", messages) + "\n");
        scroll.layout();
        scroll.setScrollPercentY(1f);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        ChatMessage msg;
        while ((msg = client.poll(ChatMessage.class)) != null) {
            addMessage(msg);
        }
    }
}
