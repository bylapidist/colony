package net.lapidist.colony.core.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.components.assets.TextureComponent;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.basic.Scale;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

public final class Mappers extends BaseSystem {

    public M<Pos> mPos;
    public M<TextureComponent> mTexture;
    public M<Tint> mTint;
    public M<Angle> mAngle;
    public M<Scale> mScale;
    public M<Origin> mOrigin;
    public M<Label> mLabel;

    @Override
    protected void initialize() {
        mPos = new M<>(Pos.class, world);
        mTexture = new M<>(TextureComponent.class, world);
        mTint = new M<>(Tint.class, world);
        mAngle = new M<>(Angle.class, world);
        mScale = new M<>(Scale.class, world);
        mOrigin = new M<>(Origin.class, world);
        mLabel = new M<>(Label.class, world);
    }

    @Override
    protected void processSystem() {

    }
}
