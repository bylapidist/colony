package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.FontComponent;
import net.lapidist.colony.components.WorldPositionComponent;
import net.lapidist.colony.components.GuiComponent;
import net.lapidist.colony.components.LabelComponent;
import net.lapidist.colony.core.EntityType;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractControlSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.assets.GuiAssetSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.physics.TimeSystem;
import net.lapidist.colony.core.ui.views.ConsoleWindow;
import net.lapidist.colony.core.systems.Events;
import net.lapidist.colony.core.systems.IListener;

import static com.artemis.E.E;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem implements IListener {

    private final ViewRenderer viewRenderer = new ViewRenderer();
    private AbstractCameraSystem cameraSystem;
    private GuiAssetSystem assetSystem;
    private EntityFactorySystem entityFactorySystem;
    private TimeSystem timeSystem;
    private int fpsCounter;
    private int season;

    public GuiRenderSystem() {
        super(Aspect.all(GuiComponent.class));
    }

    @Override
    protected void initialize() {
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

    @Override
    public void addMessageListeners() {
        MessageManager.getInstance().addListener(this, Events.RESIZE);
        MessageManager.getInstance().addListener(this, Events.GUI_INIT);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case Events.RESIZE:
                onResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                break;
            case Events.GUI_INIT:
                onInit();
                break;
        }
        return true;
    }

    protected void onResize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(false, width, height);
        cameraSystem.guiCamera.update();
    }

    protected void onInit() {
        int e = entityFactorySystem.create(entityFactorySystem.getArchetype(EntityType.LABEL));
        E(e).worldPositionComponentPosition(new Vector3(16, 32, 0));
        E(e).fontComponentFont(assetSystem.getFont("default"));
        E(e).labelComponentText("");
        E(e).sortableComponentLayer(1000);
        fpsCounter = e;

        int e2 = entityFactorySystem.create(entityFactorySystem.getArchetype(EntityType.LABEL));
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
