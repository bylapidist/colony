package net.lapidist.colony.settings;

import java.util.Properties;
/**
 * Graphics configuration flags controlling advanced rendering options.
 */
public final class GraphicsSettings {
    private static final String PREFIX = "graphics.";
    private static final String AA_KEY = PREFIX + "antialiasing";
    private static final String MIP_KEY = PREFIX + "mipmaps";
    private static final String AF_KEY = PREFIX + "anisotropic";
    static final String LEGACY_SHADER_KEY = PREFIX + "shaders";
    private static final String PLUGIN_KEY = PREFIX + "shaderPlugin";
    private static final String RENDERER_KEY = PREFIX + "renderer";
    private static final String CACHE_KEY = PREFIX + "spritecache";
    private static final String LIGHT_KEY = PREFIX + "lighting";
    private static final String NORMAL_KEY = PREFIX + "normalmaps";
    private static final String SPECULAR_KEY = PREFIX + "specularmaps";
    private static final String DAY_NIGHT_KEY = PREFIX + "dayNightCycle";

    private boolean antialiasingEnabled = true;
    private boolean mipMapsEnabled = true;
    private boolean anisotropicFilteringEnabled = true;
    private String shaderPlugin = "none";
    private String renderer = "sprite";
    private boolean spriteCacheEnabled = true;
    private boolean lightingEnabled = true;
    private boolean normalMapsEnabled;
    private boolean specularMapsEnabled;
    private boolean dayNightCycleEnabled = true;

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

    public String getShaderPlugin() {
        return shaderPlugin;
    }

    public void setShaderPlugin(final String plugin) {
        this.shaderPlugin = plugin;
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

    public boolean isLightingEnabled() {
        return lightingEnabled;
    }

    public void setLightingEnabled(final boolean enabled) {
        this.lightingEnabled = enabled;
    }

    public boolean isNormalMapsEnabled() {
        return normalMapsEnabled;
    }

    public void setNormalMapsEnabled(final boolean enabled) {
        this.normalMapsEnabled = enabled;
    }

    public boolean isSpecularMapsEnabled() {
        return specularMapsEnabled;
    }

    public void setSpecularMapsEnabled(final boolean enabled) {
        this.specularMapsEnabled = enabled;
    }

    public boolean isDayNightCycleEnabled() {
        return dayNightCycleEnabled;
    }

    public void setDayNightCycleEnabled(final boolean enabled) {
        this.dayNightCycleEnabled = enabled;
    }

    /** Load graphics settings from the given properties. */
    public static GraphicsSettings load(final Properties props) {
        GraphicsSettings gs = new GraphicsSettings();
        gs.antialiasingEnabled = Boolean.parseBoolean(props.getProperty(AA_KEY, "true"));
        gs.mipMapsEnabled = Boolean.parseBoolean(props.getProperty(MIP_KEY, "true"));
        gs.anisotropicFilteringEnabled = Boolean.parseBoolean(props.getProperty(AF_KEY, "true"));
        gs.shaderPlugin = props.getProperty(PLUGIN_KEY, "none");
        gs.renderer = props.getProperty(RENDERER_KEY, "sprite");
        gs.spriteCacheEnabled = Boolean.parseBoolean(props.getProperty(CACHE_KEY, "true"));
        gs.lightingEnabled = Boolean.parseBoolean(props.getProperty(LIGHT_KEY, "true"));
        gs.normalMapsEnabled = Boolean.parseBoolean(props.getProperty(NORMAL_KEY, "false"));
        gs.specularMapsEnabled = Boolean.parseBoolean(props.getProperty(SPECULAR_KEY, "false"));
        gs.dayNightCycleEnabled = Boolean.parseBoolean(props.getProperty(DAY_NIGHT_KEY, "true"));
        return gs;
    }

    /** Save graphics settings to the provided properties. */
    public void save(final Properties props) {
        props.setProperty(AA_KEY, Boolean.toString(antialiasingEnabled));
        props.setProperty(MIP_KEY, Boolean.toString(mipMapsEnabled));
        props.setProperty(AF_KEY, Boolean.toString(anisotropicFilteringEnabled));
        props.setProperty(PLUGIN_KEY, shaderPlugin);
        props.setProperty(RENDERER_KEY, renderer);
        props.setProperty(CACHE_KEY, Boolean.toString(spriteCacheEnabled));
        props.setProperty(LIGHT_KEY, Boolean.toString(lightingEnabled));
        props.setProperty(NORMAL_KEY, Boolean.toString(normalMapsEnabled));
        props.setProperty(SPECULAR_KEY, Boolean.toString(specularMapsEnabled));
        props.setProperty(DAY_NIGHT_KEY, Boolean.toString(dayNightCycleEnabled));
    }
}
