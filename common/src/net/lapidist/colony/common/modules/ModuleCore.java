package net.lapidist.colony.common.modules;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.lapidist.colony.common.utils.ThreadArray;

public abstract class ModuleCore extends ApplicationAdapter {
    protected ObjectMap<Class<? extends Module>, Module> modules = new ObjectMap<>();
    protected Array<Module> moduleArray = new ThreadArray<>();

    public ModuleCore() {
    }

    abstract public void init();

    public void preInit() {
    }

    public void postInit() {
    }

    public void update() {
//        Inputs.update();
    }

    /**
     * Adds a modules to the list.
     */
    protected <N extends Module> void addModule(N module) {
        modules.put(module.getClass(), module);
        moduleArray.add(module);
        module.preInit();
    }

    @Override
    public void create() {
//        Inputs.initialize();

        init();
        preInit();

        for (Module module : moduleArray) {
            module.init();
        }

        postInit();
    }

    @Override
    public void resize(int width, int height) {
//        Graphics.resize();

        for (Module module : moduleArray) {
            module.resize(width, height);
        }
    }

    @Override
    public void render() {
        for (Module module : moduleArray) {
            module.update();
        }

        update();
    }

    @Override
    public void pause() {
        for (Module module : moduleArray) {
            module.pause();
        }
    }

    @Override
    public void resume() {
        for (Module module : moduleArray) {
            module.resume();
        }
    }

    @Override
    public void dispose() {
        for (Module module : moduleArray) {
            module.dispose();
        }
    }
}
