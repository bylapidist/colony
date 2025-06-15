package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.Texture;

/** Supplies a light map texture for shaders. */
public interface LightMapProvider {
    /** Returns the texture containing precomputed lighting for the frame. */
    Texture getLightMapTexture();
}
