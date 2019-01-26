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
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.map.HoverTileWithinReachEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.map.MapGenerationSystem;

import static com.artemis.E.E;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem {

    private AbstractCameraSystem cameraSystem;
    private GuiAssetSystem assetSystem;
    private EntityFactorySystem entityFactorySystem;
    private MapGenerationSystem mapGenerationSystem;
    private int hoveredTile;

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

        E(e).labelComponentText(Gdx.graphics.getFramesPerSecond() + " FPS");

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
        E(e).labelComponentText(Gdx.graphics.getFramesPerSecond() + " FPS");
        E(e).sortableComponentLayer(1000);

        hoveredTile = entityFactorySystem.create(entityFactorySystem.getArchetype("hoveredTile"));
        e = hoveredTile;

        E(e).textureComponentTexture(assetSystem.getTexture("hoveredTile"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
        E(e).worldPositionComponentPosition(new Vector3(
                -10000,
                -10000,
                0
        ));
        E(e).scaleComponentScale(1);
        E(e).sortableComponentLayer(10);
    }

    private void onHoverTileWithinReach(int worldX, int worldY) {
        if (worldX < 0 || worldX > mapGenerationSystem.getWidth()
                || worldY < 0 || worldY > mapGenerationSystem.getHeight()) return;

        E(hoveredTile).worldPositionComponentPosition().set(
                worldX * mapGenerationSystem.getTileWidth(),
                worldY * mapGenerationSystem.getTileHeight(),
                0
        );
    }
}
