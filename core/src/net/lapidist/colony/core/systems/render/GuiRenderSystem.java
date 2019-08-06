package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.base.WorldPositionComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.components.gui.LabelComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.AbstractControlSystem;
import net.lapidist.colony.core.systems.AbstractRenderSystem;
import net.lapidist.colony.core.systems.AbstractCameraSystem;
import net.lapidist.colony.core.systems.assets.GuiAssetSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.logic.TimeSystem;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;
import net.lapidist.colony.core.views.ConsoleWindow;
import net.lapidist.colony.core.views.ViewRenderer;

import static com.artemis.E.E;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem {

    private final ViewRenderer viewRenderer = new ViewRenderer();
    private AbstractCameraSystem cameraSystem;
    private GuiAssetSystem assetSystem;
    private EntityFactorySystem entityFactorySystem;
    private MapGeneratorSystem mapGeneratorSystem;
    private TimeSystem timeSystem;
    private int fpsCounter;
    private int season;

    public GuiRenderSystem() {
        super(Aspect.all(GuiComponent.class));
    }

    @Override
    protected void initialize() {
        Events.on(GuiInitEvent.class, guiInitEvent -> onInit());
        Events.on(ScreenResizeEvent.class, event -> onResize(event.getWidth(), event.getHeight()));

        viewRenderer.create();
        viewRenderer.setView(ConsoleWindow.class);

        AbstractControlSystem
                .getInputMultiplexer()
                .addProcessor(
                    viewRenderer
                            .getCurrentView()
                            .getStage()
        );
    }

    @Override
    protected void begin() {
        batch.begin();
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
    }

    @Override
    protected void end() {
        batch.end();
        viewRenderer.render();
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
    }

    public ViewRenderer getViewRenderer() {
        return viewRenderer;
    }
}
