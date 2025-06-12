package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.i18n.I18n;

/**
 * Simple loading screen showing progress while the map and resources load.
 */
public final class LoadingScreen extends BaseScreen {
    private static final float STEP = 0.01f;
    private static final float PADDING = 10f;
    private static final float BAR_WIDTH = 300f;
    private final GameClient client;
    private final ProgressBar bar;

    public LoadingScreen(final GameClient gameClient) {
        this(gameClient, new Stage(new ScreenViewport()));
    }

    public LoadingScreen(final GameClient gameClient, final Stage stage) {
        super(stage);
        this.client = gameClient;
        Label label = new Label(I18n.get("loading.title"), getSkin());
        bar = new ProgressBar(0f, 1f, STEP, false, getSkin());

        bar.addAction(new Action() {
            @Override
            public boolean act(final float delta) {
                bar.setValue(client.getLoadProgress());
                return false;
            }
        });

        getRoot().add(label).pad(PADDING).row();
        getRoot().add(bar).width(BAR_WIDTH).pad(PADDING).row();
    }
}
