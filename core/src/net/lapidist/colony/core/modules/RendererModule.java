package net.lapidist.colony.core.modules;

import net.lapidist.colony.common.modules.Module;
import net.lapidist.colony.core.core.Core;

public class RendererModule extends Module {

    public void draw() {
    }

    @Override
    public void resize(int width, int height) {
//        camera.setToOrtho(false, width, height);
        Core.camera.viewportWidth = width;
        Core.camera.viewportHeight = height;
        resize();
    }
}
