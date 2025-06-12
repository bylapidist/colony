package net.lapidist.colony.settings;

import com.badlogic.gdx.Preferences;

/**
 * Graphics configuration flags controlling advanced rendering options.
 */
public final class GraphicsSettings {
    private static final String PREFIX = "graphics.";
    private static final String AA_KEY = PREFIX + "antialiasing";
    private static final String MIP_KEY = PREFIX + "mipmaps";
    private static final String AF_KEY = PREFIX + "anisotropic";
    private static final String SHADER_KEY = PREFIX + "shaders";
    private static final String RENDERER_KEY = PREFIX + "renderer";
    private static final String CACHE_KEY = PREFIX + "spritecache";

    private boolean antialiasingEnabled;
    private boolean mipMapsEnabled;
    private boolean anisotropicFilteringEnabled;
    private boolean shadersEnabled;
    private String renderer = "sprite";
    private boolean spriteCacheEnabled = true;

    public boolean isAntialiasingEnabled() {
        return antialiasingEnabled;
    }

    public void setAntialiasingEnabled(final boolean enabled) {
        this.antialiasingEnabled = enabled;
    }

    public boolean isMipMapsEnabled() {
        return mipMapsEnabled;
    }

    public void setMipMapsEnabled(final boolean enabled) {
        this.mipMapsEnabled = enabled;
    }

    public boolean isAnisotropicFilteringEnabled() {
        return anisotropicFilteringEnabled;
    }

    public void setAnisotropicFilteringEnabled(final boolean enabled) {
        this.anisotropicFilteringEnabled = enabled;
    }

    public boolean isShadersEnabled() {
        return shadersEnabled;
    }

    public void setShadersEnabled(final boolean enabled) {
        this.shadersEnabled = enabled;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(final String rendererToSet) {
        this.renderer = rendererToSet;
    }

    public boolean isSpriteCacheEnabled() {
        return spriteCacheEnabled;
    }

    public void setSpriteCacheEnabled(final boolean enabled) {
        this.spriteCacheEnabled = enabled;
    }

    /** Load graphics settings from the given preferences. */
    public static GraphicsSettings load(final Preferences prefs) {
        GraphicsSettings gs = new GraphicsSettings();
        gs.antialiasingEnabled = prefs.getBoolean(AA_KEY, false);
        gs.mipMapsEnabled = prefs.getBoolean(MIP_KEY, false);
        gs.anisotropicFilteringEnabled = prefs.getBoolean(AF_KEY, false);
        gs.shadersEnabled = prefs.getBoolean(SHADER_KEY, false);
        gs.renderer = prefs.getString(RENDERER_KEY, "sprite");
        gs.spriteCacheEnabled = prefs.getBoolean(CACHE_KEY, true);
        return gs;
    }

    /** Save graphics settings to the provided preferences. */
    public void save(final Preferences prefs) {
        prefs.putBoolean(AA_KEY, antialiasingEnabled);
        prefs.putBoolean(MIP_KEY, mipMapsEnabled);
        prefs.putBoolean(AF_KEY, anisotropicFilteringEnabled);
        prefs.putBoolean(SHADER_KEY, shadersEnabled);
        prefs.putString(RENDERER_KEY, renderer);
        prefs.putBoolean(CACHE_KEY, spriteCacheEnabled);
    }
}
