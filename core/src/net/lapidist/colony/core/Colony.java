package net.lapidist.colony.core;

import aurelienribon.tweenengine.TweenManager;
import com.bitfire.utils.ShaderLoader;
import net.lapidist.colony.common.io.FileLocation;
import net.lapidist.colony.common.io.ResourceLoader;
import net.lapidist.colony.core.core.*;
import net.lapidist.colony.core.tween.Accessors;
import net.lapidist.colony.core.events.EventType.GameLoadEvent;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.modules.ModuleCore;

import java.io.IOException;

import static net.lapidist.colony.core.Constants.*;

public class Colony extends ModuleCore {
    @Override
    public void init() {
        ShaderLoader.BasePath = "shaders/postprocessing/";

        try {
            resourceLoader = new ResourceLoader(
                    FileLocation.INTERNAL,
                    FileLocation.INTERNAL.getFile("resources.xml")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        addModule(logic = new Logic());
        addModule(world = new World());
        addModule(control = new Control());
        addModule(renderer = new Renderer());
        addModule(ui = new UI());

        tweenManager = new TweenManager();
        Accessors.register();
    }

    @Override
    public void postInit() {
        Events.fire(new GameLoadEvent());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        Core.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        Graphics.resize();
    }
}
