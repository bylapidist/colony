package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.TextureComponent;
import net.lapidist.colony.components.OriginComponent;
import net.lapidist.colony.components.WorldPositionComponent;
import net.lapidist.colony.components.RotationComponent;
import net.lapidist.colony.components.ScaleComponent;
import net.lapidist.colony.components.GuiComponent;
import net.lapidist.colony.components.InvisibleComponent;
import net.lapidist.colony.components.SortableComponent;
import net.lapidist.colony.core.systems.Events;
import net.lapidist.colony.core.systems.IListener;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.physics.TimeSystem;
import net.lapidist.colony.core.systems.assets.MapAssetSystem;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;
import net.lapidist.colony.core.systems.physics.MapPhysicsSystem;

import static com.artemis.E.E;

@Wire
public class MapRenderSystem extends AbstractRenderSystem implements IListener {

    private AbstractCameraSystem cameraSystem;
    private MapGeneratorSystem mapGeneratorSystem;
    private MapPhysicsSystem mapPhysicsSystem;
    private MapAssetSystem assetSystem;
    private TimeSystem timeSystem;
    private final Vector2 tmpVec2 = new Vector2();

    private final Color seasonColor = new Color();
    private final Color startingSeasonColor = new Color();
    private final Color targetSeasonColor = new Color();
    private float elapsedSeasonColorChangeTime = 0f;
    private float seasonChangeDuration = 10000f;

    private final Color timeColor = new Color();
    private final Color startingTimeColor = new Color();
    private final Color targetTimeColor = new Color();
    private float elapsedTimeColorChangeTime = 0f;
    private float timeChangeDuration = 10000f;

    public MapRenderSystem() {
        super(Aspect.all(SortableComponent.class).exclude(InvisibleComponent.class, GuiComponent.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        addMessageListeners();

        startingSeasonColor.set(timeSystem.getCurrentTime().getColor(timeSystem.getCurrentSeason()));
        targetSeasonColor.set(timeSystem.getCurrentTime().getColor(timeSystem.getCurrentSeason()));
        seasonColor.set(timeSystem.getCurrentTime().getColor(timeSystem.getCurrentSeason()));

        startingTimeColor.set(timeSystem.getCurrentTime().getAmbientLight(timeSystem.getCurrentTime()));
        targetTimeColor.set(timeSystem.getCurrentTime().getAmbientLight(timeSystem.getCurrentTime()));
        timeColor.set(timeSystem.getCurrentTime().getAmbientLight(timeSystem.getCurrentTime()));
    }

    @Override
    protected void begin() {
        batch.begin();
        batch.setProjectionMatrix(cameraSystem.camera.combined);
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void process(Entity e) {
        if (
                E(e).hasTextureComponent() &&
                E(e).hasRotationComponent() &&
                E(e).hasOriginComponent() &&
                E(e).hasWorldPositionComponent() &&
                E(e).hasScaleComponent()
        ) {
            final WorldPositionComponent posC = E(e).getWorldPositionComponent();
            final TextureComponent textureC = E(e).getTextureComponent();
            final RotationComponent rotationC = E(e).getRotationComponent();
            final ScaleComponent scaleC = E(e).getScaleComponent();
            final OriginComponent originC = E(e).getOriginComponent();

            tmpVec2.set(cameraSystem.screenCoordsFromWorldCoords(posC.getPosition().x, posC.getPosition().y));
            if (isWithinBounds(tmpVec2.x, tmpVec2.y)) {
                if (E(e).hasTerrainComponent())
                    batch.setColor(seasonColor);

                drawTexture(textureC, rotationC, originC, posC, scaleC, cameraSystem.zoom);
                batch.setColor(Color.WHITE);
            }
        }

        updateSeasonColor(Gdx.graphics.getDeltaTime());
        updateTimeColor(Gdx.graphics.getDeltaTime());
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    @Override
    public void addMessageListeners() {
        MessageManager.getInstance().addListener(this, Events.RESIZE);
        MessageManager.getInstance().addListener(this, Events.MAP_INIT);
        MessageManager.getInstance().addListener(this, Events.SEASON_CHANGE);
        MessageManager.getInstance().addListener(this, Events.TIME_CHANGE);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
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
        }
        return true;
    }

    private void updateSeasonColor(float delta) {
        if (elapsedSeasonColorChangeTime < seasonChangeDuration) {
            elapsedSeasonColorChangeTime = Math.min(elapsedSeasonColorChangeTime + delta, seasonChangeDuration);

            seasonColor.set(startingSeasonColor).lerp(targetSeasonColor,
                    Interpolation.fade.apply(elapsedSeasonColorChangeTime / seasonChangeDuration));
        }
    }

    private void updateTimeColor(float delta) {
        if (elapsedTimeColorChangeTime < timeChangeDuration) {
            elapsedTimeColorChangeTime = Math.min(elapsedTimeColorChangeTime + delta, timeChangeDuration);

            timeColor.set(startingTimeColor).lerp(targetTimeColor,
                    Interpolation.fade.apply(elapsedTimeColorChangeTime / timeChangeDuration));
        }
        mapPhysicsSystem.getRayHandler().setAmbientLight(timeColor);
    }

    private void changeSeasonColor(Color color, float duration) {
        targetSeasonColor.set(color);
        startingSeasonColor.set(seasonColor);
        elapsedSeasonColorChangeTime = 0;
        seasonChangeDuration = duration;
    }

    private void changeTimeColor(Color color, float duration) {
        targetTimeColor.set(color);
        startingTimeColor.set(timeColor);
        elapsedTimeColorChangeTime = 0;
        timeChangeDuration = duration;
    }

    protected void onResize(int width, int height) {
        cameraSystem.camera.setToOrtho(false, width, height);
        cameraSystem.camera.update();
    }

    protected void onInit() {
        mapGeneratorSystem.generate();
    }

    private void onSeasonChange(TimeSystem.Season season) {
        changeSeasonColor(timeSystem.getCurrentTime().getColor(season), seasonChangeDuration);
    }

    private void onTimeChange(TimeSystem.TimeOfDay timeOfDay) {
        changeTimeColor(timeSystem.getCurrentTime().getAmbientLight(timeOfDay), timeChangeDuration);
    }
}
