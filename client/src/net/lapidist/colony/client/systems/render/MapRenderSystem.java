package net.lapidist.colony.client.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.systems.assets.MapAssetSystem;
import net.lapidist.colony.components.*;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.IListener;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;
import net.lapidist.colony.core.systems.physics.MapPhysicsSystem;
import net.lapidist.colony.core.systems.physics.TimeSystem;

import static com.artemis.E.E;

@Wire
public class MapRenderSystem extends AbstractRenderSystem implements IListener {

    private static final float SEASON_TIME_DURATION = 10000f;
    private static final float TIME_CHANGE_DURATION = 10000f;

    private float elapsedSeasonColorChangeTime = 0f;
    private float elapsedTimeColorChangeTime = 0f;

    private final Vector2 tmpVec2 = new Vector2();
    private final Color seasonColor = new Color();
    private final Color startingSeasonColor = new Color();
    private final Color targetSeasonColor = new Color();
    private final Color timeColor = new Color();
    private final Color startingTimeColor = new Color();
    private final Color targetTimeColor = new Color();

    private AbstractCameraSystem cameraSystem;
    private EntityFactorySystem entityFactorySystem;
    private MapGeneratorSystem mapGeneratorSystem;
    private MapPhysicsSystem mapPhysicsSystem;
    private MapAssetSystem assetSystem;
    private TimeSystem timeSystem;

    public MapRenderSystem() {
        super(
                Aspect.all(SortableComponent.class)
                        .exclude(InvisibleComponent.class, GuiComponent.class)
        );
    }

    @Override
    protected final void initialize() {
        super.initialize();
        addMessageListeners();

        startingSeasonColor.set(timeSystem.getCurrentTime().getColor(
                timeSystem.getCurrentSeason()
        ));
        targetSeasonColor.set(timeSystem.getCurrentTime().getColor(
                timeSystem.getCurrentSeason()
        ));
        seasonColor.set(timeSystem.getCurrentTime().getColor(
                timeSystem.getCurrentSeason()
        ));
        startingTimeColor.set(timeSystem.getCurrentTime().getAmbientLight(
                timeSystem.getCurrentTime()
        ));
        targetTimeColor.set(timeSystem.getCurrentTime().getAmbientLight(
                timeSystem.getCurrentTime()
        ));
        timeColor.set(timeSystem.getCurrentTime().getAmbientLight(
                timeSystem.getCurrentTime()
        ));
    }

    @Override
    protected final void begin() {
        getBatch().begin();
        getBatch().setProjectionMatrix(cameraSystem.getCamera().combined);
    }

    @Override
    protected final void end() {
        getBatch().end();
    }

    @Override
    protected final void process(final Entity e) {
        if (
                E(e).hasTextureComponent()
                        && E(e).hasRotationComponent()
                        && E(e).hasOriginComponent()
                        && E(e).hasWorldPositionComponent()
                        && E(e).hasScaleComponent()
        ) {
            final WorldPositionComponent posC =
                    E(e).getWorldPositionComponent();
            final TextureComponent textureC = E(e).getTextureComponent();
            final RotationComponent rotationC = E(e).getRotationComponent();
            final ScaleComponent scaleC = E(e).getScaleComponent();
            final OriginComponent originC = E(e).getOriginComponent();

            tmpVec2.set(cameraSystem.screenCoordsFromWorldCoords(
                    posC.getPosition().x,
                    posC.getPosition().y
            ));

            if (isWithinBounds(tmpVec2.x, tmpVec2.y)) {
                if (E(e).hasTerrainComponent()) {
                    getBatch().setColor(seasonColor);
                }

                //drawTexture(
                // textureC,
                // rotationC,
                // originC,
                // posC,
                // scaleC,
                // cameraSystem.zoom
                // );
                getBatch().setColor(Color.WHITE);
            }
        }

        updateSeasonColor(Gdx.graphics.getDeltaTime());
        updateTimeColor(Gdx.graphics.getDeltaTime());
    }

    @Override
    protected final void disposeMap() {
    }

    @Override
    protected void disposePhysics() {
    }

    @Override
    protected void disposeGui() {
    }

    @Override
    public final void addMessageListeners() {
        MessageManager.getInstance().addListener(this, Events.RESIZE);
        MessageManager.getInstance().addListener(this, Events.MAP_INIT);
        MessageManager.getInstance().addListener(this, Events.SEASON_CHANGE);
        MessageManager.getInstance().addListener(this, Events.TIME_CHANGE);
    }

    @Override
    public final boolean handleMessage(final Telegram msg) {
        switch (msg.message) {
            case Events.RESIZE:
                onResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                break;
            case Events.MAP_INIT:
                onInit();
                break;
            case Events.SEASON_CHANGE:
                onSeasonChange(timeSystem.getCurrentSeason());
                break;
            case Events.TIME_CHANGE:
                onTimeChange(timeSystem.getCurrentTime());
                break;
            default:
                return false;
        }
        return false;
    }

    private void updateSeasonColor(final float delta) {
        if (elapsedSeasonColorChangeTime < SEASON_TIME_DURATION) {
            elapsedSeasonColorChangeTime =
                    Math.min(
                            elapsedSeasonColorChangeTime + delta,
                            SEASON_TIME_DURATION
                    );

            seasonColor.set(startingSeasonColor).lerp(
                    targetSeasonColor,
                    Interpolation.fade.apply(
                            elapsedSeasonColorChangeTime / SEASON_TIME_DURATION
                    )
            );
        }
    }

    private void updateTimeColor(final float delta) {
        if (elapsedTimeColorChangeTime < TIME_CHANGE_DURATION) {
            elapsedTimeColorChangeTime = Math.min(
                    elapsedTimeColorChangeTime + delta,
                    TIME_CHANGE_DURATION
            );

            timeColor.set(startingTimeColor).lerp(
                    targetTimeColor,
                    Interpolation.fade.apply(
                            elapsedTimeColorChangeTime / TIME_CHANGE_DURATION
                    )
            );
        }
        mapPhysicsSystem.getRayHandler().setAmbientLight(timeColor);
    }

    private void changeSeasonColor(final Color color) {
        targetSeasonColor.set(color);
        startingSeasonColor.set(seasonColor);
        elapsedSeasonColorChangeTime = 0;
    }

    private void changeTimeColor(final Color color) {
        targetTimeColor.set(color);
        startingTimeColor.set(timeColor);
        elapsedTimeColorChangeTime = 0;
    }

    protected final void onResize(final int width, final int height) {
        cameraSystem.getCamera().setToOrtho(false, width, height);
        cameraSystem.getCamera().update();
    }

    protected final void onInit() {
        mapGeneratorSystem.generate();
    }

    private void onSeasonChange(final TimeSystem.Season season) {
        changeSeasonColor(timeSystem.getCurrentTime().getColor(season));
    }

    private void onTimeChange(final TimeSystem.TimeOfDay timeOfDay) {
        changeTimeColor(timeSystem.getCurrentTime().getAmbientLight(timeOfDay));
    }
}
