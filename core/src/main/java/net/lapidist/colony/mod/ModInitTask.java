package net.lapidist.colony.mod;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Background task that loads mods using {@link ModLoader} and calls their {@link GameMod#init()} method.
 */
public final class ModInitTask implements Callable<List<ModLoader.LoadedMod>> {
    private final ModLoader loader;
    private final Consumer<Float> progress;

    public ModInitTask(final ModLoader loaderToUse, final Consumer<Float> progressListener) {
        this.loader = loaderToUse;
        this.progress = progressListener;
    }

    @Override
    public List<ModLoader.LoadedMod> call() {
        try {
            List<ModLoader.LoadedMod> mods = loader.loadMods();
            if (progress != null) {
                progress.accept(0f);
            }
            int total = mods.size();
            int count = 0;
            for (ModLoader.LoadedMod mod : mods) {
                mod.mod().init();
                count++;
                if (progress != null && total > 0) {
                    progress.accept(count / (float) total);
                }
            }
            if (progress != null) {
                progress.accept(1f);
            }
            return mods;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
