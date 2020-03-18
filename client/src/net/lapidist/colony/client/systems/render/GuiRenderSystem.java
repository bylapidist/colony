package net.lapidist.colony.client.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.systems.assets.GuiAssetSystem;
import net.lapidist.colony.client.windows.ConsoleWindow;
import net.lapidist.colony.components.FontComponent;
import net.lapidist.colony.components.GuiComponent;
import net.lapidist.colony.components.LabelComponent;
import net.lapidist.colony.components.WorldPositionComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.IListener;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractControlSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;
import net.lapidist.colony.core.systems.physics.TimeSystem;

import static com.artemis.E.E;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem implements IListener {

    private static final int PADDING = 16;
    private static final int SORT_LAYER = 1000;

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
    protected final void initialize() {
        addMessageListeners();
        viewRenderer.create();
        viewRenderer.setView(ConsoleWindow.class);
        AbstractControlSystem
                .getInputMultiplexer()
                .addProcessor(
                        getViewRenderer()
                                .getCurrentView()
                                .getStage()
                );
    }

    @Override
    protected final void begin() {
        getBatch().begin();
        getBatch().setProjectionMatrix(cameraSystem.getGuiCamera().combined);
    }

    @Override
    protected final void end() {
        getBatch().end();
        viewRenderer.render();
    }

    @Override
    protected final void process(final Entity e) {
        final WorldPositionComponent posC = E(e).getWorldPositionComponent();
        final LabelComponent labelC = E(e).getLabelComponent();
        final FontComponent fontC = E(e).getFontComponent();

        E(fpsCounter).labelComponentText(
                Gdx.graphics.getFramesPerSecond() + " FPS"
        );
        E(season).labelComponentText(
                String.format(
                        "Year %s, Day %s\n%s, %s",
                        String.valueOf(timeSystem.getYear()),
                        String.valueOf(timeSystem.getDay()),
                        timeSystem.getCurrentSeason().toString(),
                        timeSystem.getCurrentTime().toString()
                )
        );

        if (fontC != null) {
            drawLabel(labelC, fontC, posC);
        }
    }

    @Override
    protected final void disposeGui() {
    }

    @Override
    protected void disposeMap() {
    }

    @Override
    protected void disposePhysics() {
    }

    @Override
    public final void addMessageListeners() {
        MessageManager.getInstance().addListener(this, Events.RESIZE);
        MessageManager.getInstance().addListener(this, Events.GUI_INIT);
    }

    @Override
    public final boolean handleMessage(final Telegram msg) {
        switch (msg.message) {
            case Events.RESIZE:
                onResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                break;
            case Events.GUI_INIT:
                onInit();
                break;
            default:
                return false;
        }
        return false;
    }

    protected final void onResize(final int width, final int height) {
        cameraSystem.getGuiCamera().setToOrtho(false, width, height);
        cameraSystem.getGuiCamera().update();
    }

    protected final void onInit() {
        int e = entityFactorySystem.create(
                entityFactorySystem.getArchetype("label")
        );

        E(e).worldPositionComponentPosition(
                new Vector3(PADDING, PADDING * 2, 0)
        );
        E(e).fontComponentFont(assetSystem.getFont("default"));
        E(e).labelComponentText("");
        E(e).sortableComponentLayer(SORT_LAYER);
        fpsCounter = e;

        int e2 = entityFactorySystem.create(
                entityFactorySystem.getArchetype("label")
        );
        E(e2).worldPositionComponentPosition(
                new Vector3(PADDING, Gdx.graphics.getHeight() - PADDING, 0)
        );
        E(e2).fontComponentFont(assetSystem.getFont("default"));
        E(e2).labelComponentText("");
        E(e2).sortableComponentLayer(SORT_LAYER);
        season = e2;
    }

    public final ViewRenderer getViewRenderer() {
        return viewRenderer;
    }
}
