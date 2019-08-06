package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.OriginComponent;
import net.lapidist.colony.components.base.WorldPositionComponent;
import net.lapidist.colony.components.base.RotationComponent;
import net.lapidist.colony.components.base.ScaleComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.components.render.InvisibleComponent;
import net.lapidist.colony.components.base.SortableComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.MapInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.events.time.SeasonChangeEvent;
import net.lapidist.colony.core.events.time.TimeChangeEvent;
import net.lapidist.colony.core.systems.AbstractRenderSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.AbstractCameraSystem;
import net.lapidist.colony.core.systems.logic.TimeSystem;
import net.lapidist.colony.core.systems.assets.MapAssetSystem;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;
import net.lapidist.colony.core.systems.physics.MapPhysicsSystem;

import static com.artemis.E.E;

@Wire
public class MapRenderSystem extends AbstractRenderSystem {

    private AbstractCameraSystem cameraSystem;
    private EntityFactorySystem entityFactorySystem;
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

        startingSeasonColor.set(timeSystem.getCurrentTime().getColor(timeSystem.getCurrentSeason()));
        targetSeasonColor.set(timeSystem.getCurrentTime().getColor(timeSystem.getCurrentSeason()));
        seasonColor.set(timeSystem.getCurrentTime().getColor(timeSystem.getCurrentSeason()));

        startingTimeColor.set(timeSystem.getCurrentTime().getAmbientLight(timeSystem.getCurrentTime()));
        targetTimeColor.set(timeSystem.getCurrentTime().getAmbientLight(timeSystem.getCurrentTime()));
        timeColor.set(timeSystem.getCurrentTime().getAmbientLight(timeSystem.getCurrentTime()));

        Events.on(MapInitEvent.class, mapInitEvent -> onInit());
        Events.on(ScreenResizeEvent.class, event -> onResize(event.getWidth(), event.getHeight()));
        Events.on(SeasonChangeEvent.class, event -> onSeasonChange(event.getSeason()));
        Events.on(TimeChangeEvent.class, event -> onTimeChange(event.getTimeOfDay()));
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

        if (E(e).hasPlayerComponent()) {
            if (
                timeSystem.getCurrentTime() == TimeSystem.TimeOfDay.NIGHT
                || timeSystem.getCurrentTime() == TimeSystem.TimeOfDay.DAWN
                || timeSystem.getCurrentTime() == TimeSystem.TimeOfDay.EVENING
                || timeSystem.getCurrentTime() == TimeSystem.TimeOfDay.DUSK
            ) {
                E(e).coneLightComponentConeLights().get(0).setColor(new Color(1, 1, 1, 0.5f));
            } else {
                E(e).coneLightComponentConeLights().get(0).setColor(Color.CLEAR);
            }
        }

        updateSeasonColor(Gdx.graphics.getDeltaTime());
        updateTimeColor(Gdx.graphics.getDeltaTime());
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

    @Override
    protected void dispose() {
        super.dispose();
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
