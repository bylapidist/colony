package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.ChatMessageData;
import net.lapidist.colony.i18n.I18n;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.lapidist.colony.client.events.PauseEvent;
import net.lapidist.colony.core.events.Events;

/**
 * Simple chat log UI with an input field.
 */
public final class ChatLogActor extends Table {
    private final GameClient client;
    private final Skin skin;
    private final CommandDispatcher<Object> dispatcher;
    private final Table messages = new Table();
    private final TextField input;

    public ChatLogActor(final GameClient clientToUse,
                        final Skin skinToUse,
                        final CommandDispatcher<Object> dispatcherToUse) {
        this.client = clientToUse;
        this.skin = skinToUse;
        this.dispatcher = dispatcherToUse;
        defaults().left();
        input = new TextField("", skin);
        input.setMessageText(I18n.get("chat.placeholder"));
        input.setTextFieldListener((field, c) -> {
            if (c == '\n' || c == '\r') {
                submit();
            }
        });
        add(messages).growX().row();
        add(input).growX();
        dispatcher.register(LiteralArgumentBuilder.<Object>literal("pause")
                .executes(ctx -> {
                    Events.dispatch(new PauseEvent());
                    return 1;
                }));
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        ChatMessageData msg;
        while ((msg = client.pollChatMessage()) != null) {
            addMessage(msg.player() + ": " + msg.message());
        }
    }

    private void submit() {
        String text = input.getText();
        input.setText("");
        if (text.isBlank()) {
            return;
        }
        if (text.startsWith("/")) {
            try {
                dispatcher.execute(text.substring(1), new Object());
            } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
                addMessage(e.getMessage());
            }
        } else {
            client.sendChatMessage(new ChatMessageData("Player", text));
        }
    }

    public void addMessage(final String text) {
        messages.add(new Label(text, skin)).left().row();
    }

    public int getMessageCount() {
        return messages.getChildren().size;
    }
}
