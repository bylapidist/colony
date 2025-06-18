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
    private final CheckBox normalBox;
    private final CheckBox specularBox;
    private final SelectBox<String> rendererBox;
    private final SelectBox<String> resolutionBox;
    private final CheckBox fullscreenBox;
    private final CheckBox cacheBox;
    private final CheckBox lightingBox;
    private final CheckBox dayNightBox;
    private static final float PADDING = 10f;

    public GraphicsSettingsScreen(final Colony game) {
        this(game, new Stage(new ScreenViewport()));
    }

    public GraphicsSettingsScreen(final Colony game, final Stage stage) {
        super(stage);
        this.colony = game;

        GraphicsSettings graphics = colony.getSettings().getGraphicsSettings();
        net.lapidist.colony.settings.Settings general = colony.getSettings();
        Table root = getRoot();

        aaBox = new CheckBox(I18n.get("graphics.antialiasing"), getSkin());
        aaBox.setChecked(graphics.isAntialiasingEnabled());
        mipBox = new CheckBox(I18n.get("graphics.mipmaps"), getSkin());
        mipBox.setChecked(graphics.isMipMapsEnabled());
        afBox = new CheckBox(I18n.get("graphics.anisotropic"), getSkin());
        afBox.setChecked(graphics.isAnisotropicFilteringEnabled());
        normalBox = new CheckBox(I18n.get("graphics.normalmaps"), getSkin());
        normalBox.setChecked(graphics.isNormalMapsEnabled());
        specularBox = new CheckBox(I18n.get("graphics.specularmaps"), getSkin());
        specularBox.setChecked(graphics.isSpecularMapsEnabled());
        rendererBox = new SelectBox<>(getSkin());
        rendererBox.setItems("sprite");
        rendererBox.setSelected(graphics.getRenderer());
        resolutionBox = new SelectBox<>(getSkin());
        com.badlogic.gdx.utils.Array<String> resolutions = new com.badlogic.gdx.utils.Array<>(new String[] {
                "1280x720", "1600x900", "1920x1080"
        });
        String resText = general.getWidth() + "x" + general.getHeight();
        if (!resolutions.contains(resText, false)) {
            resolutions.add(resText);
        }
        resolutionBox.setItems(resolutions);
        resolutionBox.setSelected(resText);
        fullscreenBox = new CheckBox(I18n.get("graphics.fullscreen"), getSkin());
        fullscreenBox.setChecked(general.isFullscreen());
        cacheBox = new CheckBox(I18n.get("graphics.spritecache"), getSkin());
        cacheBox.setChecked(graphics.isSpriteCacheEnabled());
        lightingBox = new CheckBox(I18n.get("graphics.lighting"), getSkin());
        lightingBox.setChecked(graphics.isLightingEnabled());
        dayNightBox = new CheckBox(I18n.get("graphics.dayNightCycle"), getSkin());
        dayNightBox.setChecked(graphics.isDayNightCycleEnabled());

        TextButton save = new TextButton(I18n.get("common.save"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());

        Table options = new Table();
        options.add(aaBox).left().row();
        options.add(mipBox).left().row();
        options.add(afBox).left().row();
        options.add(normalBox).left().row();
        options.add(specularBox).left().row();
        options.add(cacheBox).left().row();
        options.add(lightingBox).left().row();
        options.add(dayNightBox).left().row();
        options.add(rendererBox).left().row();
        options.add(resolutionBox).left().row();
        options.add(fullscreenBox).left().row();

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
                graphics.setNormalMapsEnabled(normalBox.isChecked());
                graphics.setSpecularMapsEnabled(specularBox.isChecked());
                graphics.setSpriteCacheEnabled(cacheBox.isChecked());
                graphics.setLightingEnabled(lightingBox.isChecked());
                graphics.setDayNightCycleEnabled(dayNightBox.isChecked());
                graphics.setRenderer(rendererBox.getSelected());
                String[] res = resolutionBox.getSelected().split("x");
                general.setWidth(Integer.parseInt(res[0]));
                general.setHeight(Integer.parseInt(res[1]));
                general.setFullscreen(fullscreenBox.isChecked());
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
