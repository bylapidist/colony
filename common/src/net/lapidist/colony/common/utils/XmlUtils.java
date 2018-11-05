package net.lapidist.colony.common.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class XmlUtils {
    public XmlUtils() {
        new XmlUtils();
    }

    public static TextureRegion getTexReg(Texture tex, String str) {
        try {
            String[] strs = str.split("\\s");
            if (strs.length != 4)
                throw new RuntimeException("Need x, y, width and height");
            int x = Integer.parseInt(strs[0]);
            int y = Integer.parseInt(strs[1]);
            int w = Integer.parseInt(strs[2]);
            int h = Integer.parseInt(strs[3]);

            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            return new TextureRegion(tex, x, y, w, h);
        } catch (Exception e) {
            System.err.println("Couldn't parse XML Resource: " + e);
            System.exit(1);
            return null;
        }
    }
}
