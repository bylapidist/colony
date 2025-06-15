package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.mod.ModLoader.LoadedMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Screen allowing users to enable or disable loaded mods.
 */
public final class ModSelectionScreen extends BaseScreen {
    private final Colony colony;
    private final List<LoadedMod> mods;
    private final Map<LoadedMod, CheckBox> boxes = new HashMap<>();
    private static final float PADDING = 10f;

    public ModSelectionScreen(final Colony game) {
        this.colony = game;
        this.mods = game.getMods();
        getRoot().add(new com.badlogic.gdx.scenes.scene2d.ui.Label(
                I18n.get("modSelect.title"), getSkin())).row();

        Table list = new Table();
        for (LoadedMod mod : mods) {
            String label = mod.metadata().id();
            CheckBox box = new CheckBox(label, getSkin());
            box.setChecked(true);
            if (label.startsWith("base-")) {
                box.setDisabled(true);
            }
            boxes.put(mod, box);
            list.add(box).left().row();
        }

        ScrollPane scroll = new ScrollPane(list, getSkin());
        scroll.setScrollingDisabled(true, false);
        getRoot().add(scroll).expand().fill().row();

        TextButton back = new TextButton(I18n.get("modSelect.back"), getSkin());
        TextButton next = new TextButton(I18n.get("modSelect.next"), getSkin());

        Table buttons = new Table();
        buttons.add(back).padRight(PADDING);
        buttons.add(next);
        getRoot().add(buttons).padBottom(PADDING).bottom();

        next.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                List<LoadedMod> enabled = new ArrayList<>();
                for (LoadedMod mod : mods) {
                    CheckBox box = boxes.get(mod);
                    if (box.isChecked() || box.isDisabled()) {
                        enabled.add(mod);
                    }
                }
                colony.setSelectedMods(enabled);
                colony.setScreen(new NewGameScreen(colony));
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });

        getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    colony.setScreen(new MainMenuScreen(colony));
                    return true;
                }
                return false;
            }
        });
    }
}
