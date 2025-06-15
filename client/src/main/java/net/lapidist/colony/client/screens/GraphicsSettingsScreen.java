package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.graphics.ShaderPluginLoader;
import net.lapidist.colony.util.I18n;
import net.lapidist.colony.settings.GraphicsSettings;

import java.io.IOException;

/**
 * Screen allowing modification of graphics options.
 */
public final class GraphicsSettingsScreen extends BaseScreen {
    private final Colony colony;
    private final CheckBox aaBox;
    private final CheckBox mipBox;
    private final CheckBox afBox;
    private final SelectBox<String> shaderBox;
    private final com.badlogic.gdx.utils.Array<String> pluginIds;
    private final CheckBox cacheBox;
    private final SelectBox<String> rendererBox;
    private static final float PADDING = 10f;

    public GraphicsSettingsScreen(final Colony game) {
        this(game, new Stage(new ScreenViewport()));
    }

    public GraphicsSettingsScreen(final Colony game, final Stage stage) {
        super(stage);
        this.colony = game;

        GraphicsSettings graphics = colony.getSettings().getGraphicsSettings();
        Table root = getRoot();

        aaBox = new CheckBox(I18n.get("graphics.antialiasing"), getSkin());
        aaBox.setChecked(graphics.isAntialiasingEnabled());
        mipBox = new CheckBox(I18n.get("graphics.mipmaps"), getSkin());
        mipBox.setChecked(graphics.isMipMapsEnabled());
        afBox = new CheckBox(I18n.get("graphics.anisotropic"), getSkin());
        afBox.setChecked(graphics.isAnisotropicFilteringEnabled());
        shaderBox = new SelectBox<>(getSkin());
        var plugins = new ShaderPluginLoader().loadPlugins();
        pluginIds = new com.badlogic.gdx.utils.Array<>();
        com.badlogic.gdx.utils.Array<String> names = new com.badlogic.gdx.utils.Array<>();
        pluginIds.add("none");
        names.add("None");
        for (var p : plugins) {
            pluginIds.add(p.id());
            names.add(p.displayName());
        }
        shaderBox.setItems(names);
        int selected = pluginIds.indexOf(graphics.getShaderPlugin(), false);
        if (selected >= 0) {
            shaderBox.setSelectedIndex(selected);
        }
        cacheBox = new CheckBox(I18n.get("graphics.spritecache"), getSkin());
        cacheBox.setChecked(graphics.isSpriteCacheEnabled());

        rendererBox = new SelectBox<>(getSkin());
        rendererBox.setItems("sprite");
        rendererBox.setSelected(graphics.getRenderer());

        TextButton save = new TextButton(I18n.get("common.save"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());

        Table options = new Table();
        options.add(aaBox).left().row();
        options.add(mipBox).left().row();
        options.add(afBox).left().row();
        options.add(shaderBox).left().row();
        options.add(cacheBox).left().row();
        options.add(rendererBox).left().row();

        ScrollPane scroll = new ScrollPane(options, getSkin());
        scroll.setScrollingDisabled(true, false);
        root.add(scroll).expand().fill().row();

        Table buttons = new Table();
        buttons.add(back).padRight(PADDING);
        buttons.add(save);

        root.add(buttons).padBottom(PADDING).bottom();

        save.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                graphics.setAntialiasingEnabled(aaBox.isChecked());
                graphics.setMipMapsEnabled(mipBox.isChecked());
                graphics.setAnisotropicFilteringEnabled(afBox.isChecked());
                int idx = shaderBox.getSelectedIndex();
                graphics.setShaderPlugin(pluginIds.get(idx));
                graphics.setSpriteCacheEnabled(cacheBox.isChecked());
                graphics.setRenderer(rendererBox.getSelected());
                save();
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new SettingsScreen(colony));
            }
        });
    }

    private void save() {
        try {
            colony.getSettings().save();
        } catch (IOException e) {
            // ignore
        }
    }
}
