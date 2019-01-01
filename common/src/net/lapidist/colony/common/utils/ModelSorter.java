package net.lapidist.colony.common.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class ModelSorter implements RenderableSorter, Comparator<Renderable> {

    private Camera camera;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    @Override
    public void sort(final Camera camera, final Array<Renderable> renderables) {
        this.camera = camera;

        renderables.sort(this);
    }

    private void getTranslation(Matrix4 worldTransform, Vector3 center, Vector3 output) {
        if (center.isZero()) {
            worldTransform.getTranslation(output);
        } else if (!worldTransform.hasRotationOrScaling()) {
            worldTransform.getTranslation(output).add(center);
        } else {
            output.set(center).mul(worldTransform);
        }
    }

    @Override
    public int compare(final Renderable r1, final Renderable r2) {
        final boolean blended1 = r1.material.has(BlendingAttribute.Type)
                && ((BlendingAttribute) r1.material.get(BlendingAttribute.Type)).blended;

        final boolean blended2 = r2.material.has(BlendingAttribute.Type)
                && ((BlendingAttribute) r2.material.get(BlendingAttribute.Type)).blended;

        if (blended1 != blended2) return blended1 ? 1 : -1;

        getTranslation(r1.worldTransform, r1.meshPart.center, tmpV1);
        getTranslation(r2.worldTransform, r2.meshPart.center, tmpV2);

        final float distance = (int) (1000f * camera.position.dst2(tmpV1)) - (int) (-1000f * camera.position.dst2(tmpV2));
        final int result = distance < 0 ? -1 : (distance > 0 ? 1 : 0);

        return blended1 ? -result : result;
    }
}
