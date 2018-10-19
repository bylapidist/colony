package net.lapidist.colony.shaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;

import static net.lapidist.colony.Constants.*;

public class SunShader extends DefaultShader {

    private final static String vert = resourceLoader.getShader("sunVertShader");
    private final static String frag = resourceLoader.getShader("sunFragShader");

    private final Texture noiseTexture = resourceLoader.getRegion("noise").getTexture();
    private final int uNoiseTexture = register(new Uniform("u_noiseTexture", TextureAttribute.Diffuse));

    public SunShader(Renderable renderable) {
        super(renderable, new Config(vert, frag));

//        set(uNoiseTexture, noiseTexture.glTarget);
    }
}
