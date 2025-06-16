package net.lapidist.colony.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.util.I18n;
import net.lapidist.colony.events.autosave.AutosaveEvent;
import net.mostlyoriginal.api.event.common.Subscribe;

/** Label displayed while the game state is being saved. */
public final class AutosaveLabel extends Label {
    private static final float DISPLAY_TIME = 2f;
    private float timer;

    public AutosaveLabel(final Skin skin) {
        super("", skin);
        setName("savingLabel");
        setVisible(false);
        if (Events.getInstance() != null) {
            Events.getInstance().registerEvents(this);
        }
    }

    @Subscribe
    private void onAutosave(final AutosaveEvent event) {
        setText(I18n.get("ui.saving"));
        timer = DISPLAY_TIME;
        setVisible(true);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        if (isVisible()) {
            timer -= delta;
            if (timer <= 0f) {
                setVisible(false);
            }
        }
    }
}
