package net.lapidist.colony.modules;

import static net.lapidist.colony.core.Core.camera;

public class RendererModule extends Module {

    public void draw() {
    }

    @Override
    public void resize(int width, int height) {
//        camera.setToOrtho(false, width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        resize();
    }
}
