package net.lapidist.colony.common.postprocessing.effects;

import net.lapidist.colony.common.postprocessing.PostProcessorEffect;

public abstract class Antialiasing extends PostProcessorEffect {

    public abstract void setViewportSize(int width, int height);
}
