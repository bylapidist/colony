package net.lapidist.colony.common.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;

public class SpriteAnimation implements Disposable {
    private final Texture texture;
    private final float[] delays;
    private final float totalDelays;
    private final TextureRegion[] keyframes;
    private final String name;
    private boolean disposed;
    private float time;
    private int frame;

    public SpriteAnimation(SpriteAnimation other) {
        this.texture = other.texture;
        this.delays = other.delays;
        this.totalDelays = other.totalDelays;
        this.keyframes = other.keyframes;
        this.name = other.name;
    }

    public SpriteAnimation(Texture texture, XmlReader.Element elem) throws RuntimeException {
        this.texture = texture;
        this.name = elem.get("name", "noname");
        int frames = elem.getChildCount();

        if (frames == 0)
            throw new RuntimeException("animation is empty");

        delays = new float[frames];
        keyframes = new TextureRegion[frames];

        if (elem.getAttributes().containsKey("delay")) {
            setDelay(elem.getFloat("delay"));
        }

        for (int i = 0; i < elem.getChildCount(); i++) {
            XmlReader.Element child = elem.getChild(i);

            keyframes[i] = XmlUtils.getTexReg(this.texture, child.getAttribute("bounds"));

            if (child.getAttributes().containsKey("delay")) {
                delays[i] = child.getFloat("delay");
            }
        }
        float totalDelay = 0;
        for (float delay : delays) {
            totalDelay += delay;
        }
        totalDelays = totalDelay;
    }

    public void setDelay(float delay) {
        for (int i = 0; i < delays.length; i++) {
            delays[i] = delay;
        }
    }

    public String getName() {
        return name;
    }

    public int getCurrentFrame() {
        float lookupTime = time % totalDelays;
        float visitedTime = 0;

        for (int i = 0; i < delays.length - 1; i++) {
            visitedTime += delays[i];
            if (lookupTime < visitedTime) {
                return i;
            }
        }
        return 0;
    }

    public void tick(float delta) {
        time += delta;
        frame = getCurrentFrame();
    }

    public TextureRegion getCurrentKeyframe() {
        return keyframes[frame];
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            texture.dispose();
        }
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void reset() {
        setFrame(0);
        setTime(0f);
    }
}
