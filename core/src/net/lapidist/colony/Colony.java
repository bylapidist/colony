package net.lapidist.colony;

import aurelienribon.tweenengine.TweenManager;
import com.bitfire.utils.ShaderLoader;
import net.lapidist.colony.core.*;
import net.lapidist.colony.event.EventType.GameLoadEvent;
import net.lapidist.colony.event.Events;
import net.lapidist.colony.io.FileLocation;
import net.lapidist.colony.io.ResourceLoader;
import net.lapidist.colony.module.ModuleCore;
import net.lapidist.colony.tween.Accessors;

import java.io.IOException;

import static net.lapidist.colony.Constants.*;

public class Colony extends ModuleCore {
    @Override
    public void init() {
        addModule(logic = new Logic());
        addModule(world = new World());
        addModule(control = new Control());
        addModule(renderer = new Renderer());
        addModule(ui = new UI());

        tweenManager = new TweenManager();
        Accessors.register();
        ShaderLoader.BasePath = "shader/postprocessing/";

        try {
            resourceLoader = new ResourceLoader(
                FileLocation.INTERNAL,
                FileLocation.INTERNAL.getFile("resources.xml")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postInit() {
        Events.fire(new GameLoadEvent());
    }

    @Override
    public void render() {
        super.render();
    }
}
