package net.lapidist.colony.tests.core.io;

import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.ResourceConfig;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/** Tests for {@link ResourceConfig}. */
public class ResourceConfigTest {

    private static Method method(final Class<?> cls, final String name, final Class<?>... types) throws Exception {
        Method m = cls.getDeclaredMethod(name, types);
        m.setAccessible(true);
        return m;
    }

    @Test
    public void storesAndRetrievesTexturesAndSounds() throws Exception {
        ResourceConfig.ResourceTextureRegion region1 = new ResourceConfig.ResourceTextureRegion();
        method(ResourceConfig.ResourceTextureRegion.class, "setName", String.class).invoke(region1, "r1");
        method(ResourceConfig.ResourceTextureRegion.class, "setBounds", String.class).invoke(region1, "0,0,32,32");

        ResourceConfig.ResourceTextureRegion region2 = new ResourceConfig.ResourceTextureRegion();
        method(ResourceConfig.ResourceTextureRegion.class, "setName", String.class).invoke(region2, "r2");
        method(ResourceConfig.ResourceTextureRegion.class, "setBounds", String.class).invoke(region2, "32,0,32,32");

        Array<ResourceConfig.ResourceTextureRegion> regions1 = new Array<>(
                new ResourceConfig.ResourceTextureRegion[]{region1}
        );
        Array<ResourceConfig.ResourceTextureRegion> regions2 = new Array<>(
                new ResourceConfig.ResourceTextureRegion[]{region2}
        );

        ResourceConfig.ResourceTexture tex1 = new ResourceConfig.ResourceTexture();
        method(ResourceConfig.ResourceTexture.class, "setName", String.class).invoke(tex1, "tex1");
        method(ResourceConfig.ResourceTexture.class, "setFilePath", String.class).invoke(tex1, "path1.png");
        method(ResourceConfig.ResourceTexture.class, "setTextureRegions", Array.class).invoke(tex1, regions1);

        ResourceConfig.ResourceTexture tex2 = new ResourceConfig.ResourceTexture();
        method(ResourceConfig.ResourceTexture.class, "setName", String.class).invoke(tex2, "tex2");
        method(ResourceConfig.ResourceTexture.class, "setFilePath", String.class).invoke(tex2, "path2.png");
        method(ResourceConfig.ResourceTexture.class, "setTextureRegions", Array.class).invoke(tex2, regions2);

        ResourceConfig.ResourceSound sound1 = new ResourceConfig.ResourceSound();
        method(ResourceConfig.ResourceSound.class, "setName", String.class).invoke(sound1, "s1");
        method(ResourceConfig.ResourceSound.class, "setFilePath", String.class).invoke(sound1, "s1.ogg");

        ResourceConfig.ResourceSound sound2 = new ResourceConfig.ResourceSound();
        method(ResourceConfig.ResourceSound.class, "setName", String.class).invoke(sound2, "s2");
        method(ResourceConfig.ResourceSound.class, "setFilePath", String.class).invoke(sound2, "s2.ogg");

        Array<ResourceConfig.ResourceTexture> textures = new Array<>(new ResourceConfig.ResourceTexture[]{tex1, tex2});
        Array<ResourceConfig.ResourceSound> sounds = new Array<>(new ResourceConfig.ResourceSound[]{sound1, sound2});

        ResourceConfig config = new ResourceConfig();
        method(ResourceConfig.class, "setTextures", Array.class).invoke(config, textures);
        method(ResourceConfig.class, "setSounds", Array.class).invoke(config, sounds);

        assertSame(textures, config.getTextures());
        assertSame(sounds, config.getSounds());

        assertSame(tex1, config.getTexture(0));
        assertSame(tex2, config.getTexture(1));
        assertSame(sound1, config.getSound(0));
        assertSame(sound2, config.getSound(1));

        assertSame(region1, config.getTextureRegion(0, 0));
        assertSame(region2, config.getTextureRegion(1, 0));
        assertSame(regions1, config.getTextureRegions(0));
        assertSame(regions2, config.getTextureRegions(1));
    }
}
