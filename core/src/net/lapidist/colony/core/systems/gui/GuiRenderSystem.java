package net.lapidist.colony.core.systems.gui;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.base.WorldPositionComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.components.gui.LabelComponent;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.map.HoverTileWithinReachEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.logic.TimeSystem;
import net.lapidist.colony.core.systems.map.MapGenerationSystem;

import static com.artemis.E.E;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem {

    private AbstractCameraSystem cameraSystem;
    private GuiAssetSystem assetSystem;
    private EntityFactorySystem entityFactorySystem;
    private MapGenerationSystem mapGenerationSystem;
    private TimeSystem timeSystem;
//    private int hoveredTile;
    private int fpsCounter;
    private int season;

    public GuiRenderSystem() {
        super(Aspect.all(GuiComponent.class));
    }

    @Override
    protected void initialize() {
        Events.on(GuiInitEvent.class, guiInitEvent -> onInit());
        Events.on(ScreenResizeEvent.class, event -> onResize(event.getWidth(), event.getHeight()));
        Events.on(HoverTileWithinReachEvent.class, event -> onHoverTileWithinReach(event.getGridX(), event.getGridY()));
    }

    @Override
    protected void begin() {
        batch.begin();
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void process(Entity e) {
        final WorldPositionComponent posC = E(e).getWorldPositionComponent();
        final LabelComponent labelC = E(e).getLabelComponent();
        final FontComponent fontC = E(e).getFontComponent();

        E(fpsCounter).labelComponentText(Gdx.graphics.getFramesPerSecond() + " FPS");
        E(season).labelComponentText(
                "Year " + String.valueOf(timeSystem.getYear()) + ", " +
                "Day " + String.valueOf(timeSystem.getDay()) + "\n" +
                timeSystem.getCurrentSeason().toString() + ", " +
                timeSystem.getCurrentTime().toString()
        );

        if (fontC != null) drawLabel(labelC, fontC, posC);
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    protected void onResize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(false, width, height);
        cameraSystem.guiCamera.update();
    }

    protected void onInit() {
        int e = entityFactorySystem.create(entityFactorySystem.getArchetype("label"));
        E(e).worldPositionComponentPosition(new Vector3(16, 32, 0));
        E(e).fontComponentFont(assetSystem.getFont("default"));
        E(e).labelComponentText("");
        E(e).sortableComponentLayer(1000);
        fpsCounter = e;

        int e2 = entityFactorySystem.create(entityFactorySystem.getArchetype("label"));
        E(e2).worldPositionComponentPosition(new Vector3(16, Gdx.graphics.getHeight() - 16, 0));
        E(e2).fontComponentFont(assetSystem.getFont("default"));
        E(e2).labelComponentText("");
        E(e2).sortableComponentLayer(1000);
        season = e2;

//        hoveredTile = entityFactorySystem.create(entityFactorySystem.getArchetype("hoveredTile"));
//        E(hoveredTile).textureComponentTexture(assetSystem.getTexture("hoveredTile"));
//        E(hoveredTile).rotationComponentRotation(0);
//        E(hoveredTile).originComponentOrigin(new Vector2(0.5f, 0.5f));
//        E(hoveredTile).worldPositionComponentPosition(new Vector3(
//                -10000,
//                -10000,
//                0
//        ));
//        E(hoveredTile).scaleComponentScale(1);
//        E(hoveredTile).sortableComponentLayer(10);
    }

    private void onHoverTileWithinReach(int worldX, int worldY) {
//        if (worldX < 0 || worldX > mapGenerationSystem.getWidth()
//                || worldY < 0 || worldY > mapGenerationSystem.getHeight()) return;

//        E(hoveredTile).worldPositionComponentPosition().set(
//                worldX * Constants.PPM,
//                worldY * Constants.PPM,
//                0
//        );
    }
}
