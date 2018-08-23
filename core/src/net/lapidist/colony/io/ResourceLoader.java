package net.lapidist.colony.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import net.lapidist.colony.utils.SpriteAnimation;
import net.lapidist.colony.utils.XmlUtils;

import java.io.IOException;

public class ResourceLoader implements Disposable {

    private boolean disposed;

    private final ObjectMap<String, TextureRegion> regions;
    private final ObjectMap<String, SpriteAnimation> animations;
    private final ObjectMap<String, Sound> sounds;
    private final FileLocation fileLocation;

    public ResourceLoader(FileLocation fileLocation, FileHandle resourceXml) throws IOException {
        this.fileLocation = fileLocation;

        regions = new ObjectMap<String, TextureRegion>();
        animations = new ObjectMap<String, SpriteAnimation>();
        sounds = new ObjectMap<String, Sound>();

        XmlReader.Element resources = new XmlReader().parse(resourceXml);
        readResourcesTag(resources);
    }

    public TextureRegion getRegion(String name) {
        return regions.get(name);
    }

    public SpriteAnimation getAnimation(String name) {
        return new SpriteAnimation(animations.get(name));
    }

    public Sound getSound(String name) {
        return sounds.get(name);
    }

    public FileLocation getFileLocation() {
        return fileLocation;
    }

    private void readResourcesTag(XmlReader.Element resources) throws RuntimeException {
        for (int i = 0; i < resources.getChildCount(); i++) {
            XmlReader.Element child = resources.getChild(i);

            switch (child.getName()) {
                case "images":
                    readImagesTag(child);
                    break;
                case "sounds":
                    readSoundsTag(child);
                    break;
                default:
                    throw new RuntimeException("Expected <images> tag.");
            }
        }
    }

    private void readImagesTag(XmlReader.Element images) throws RuntimeException {
        for (int i = 0; i < images.getChildCount(); i++) {
            XmlReader.Element child = images.getChild(i);

            switch (child.getName()) {
                case "image":
                    readImageTag(child);
                    break;
                default:
                    throw new RuntimeException("Expected <image> tag.");
            }
        }
    }

    private void readImageTag(XmlReader.Element image) throws RuntimeException {
        if (!image.getAttributes().containsKey("file"))
            throw new RuntimeException("need file=\"...\" attribute");

        Texture tex = new Texture(fileLocation.getFile(image.get("file")));
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        for (int i = 0; i < image.getChildCount(); i++) {
            XmlReader.Element child = image.getChild(i);

            switch (child.getName()) {
                case "region":
                    readRegionTag(tex, child);
                    break;
                case "animation":
                    readAnimationTag(tex, child);
                    break;
                default:
                    throw new RuntimeException("Expected <region> or <animation> tag.");
            }
        }
    }

    private void readSoundsTag(XmlReader.Element sounds) throws RuntimeException {
        for (int i = 0; i < sounds.getChildCount(); i++) {
            XmlReader.Element child = sounds.getChild(i);

            switch (child.getName()) {
                case "sound":
                    readSoundTag(child);
                    break;
                default:
                    throw new RuntimeException("Expected <sound> tag.");
            }
        }
    }

    private void readSoundTag(XmlReader.Element sound) {
        if (!sound.getAttributes().containsKey("file") || !sound.getAttributes().containsKey("name"))
            throw new RuntimeException("need file=\"...\" and name=\"...\" properties");

        Sound soundFile = Gdx.audio.newSound(fileLocation.getFile(sound.get("file")));
        sounds.put(sound.get("name"), soundFile);
    }

    private void readRegionTag(Texture texture, XmlReader.Element region) throws RuntimeException {
        if (!region.getAttributes().containsKey("name") || !region.getAttributes().containsKey("bounds"))
            throw new RuntimeException("need name=\"...\" and bounds=\"...\" properties");

        regions.put(
            region.get("name"),
            XmlUtils.getTexReg(texture, region.get("bounds"))
        );
    }

    private void readAnimationTag(Texture texture, XmlReader.Element animation) throws RuntimeException {
        if (!animation.getAttributes().containsKey("name"))
            throw new RuntimeException("need name=\"...\" property");

        animations.put(animation.get("name"), new SpriteAnimation(texture, animation));
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;

            for (ObjectMap.Entry<String, TextureRegion> entry : regions.entries()) {
                entry.value.getTexture().dispose();
            }

            for (ObjectMap.Entry<String, SpriteAnimation> entry : animations.entries()) {
                entry.value.dispose();
            }
        }
    }
}
