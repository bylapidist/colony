package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class DynamicBodyComponent extends Component {

    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Body body;

    public DynamicBodyComponent() {
        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    public DynamicBodyComponent(
            final BodyDef bodyDefToSet,
            final FixtureDef fixtureDefToSet
    ) {
        this();

        setBodyDef(bodyDefToSet);
        setFixtureDef(fixtureDefToSet);
    }

    public final BodyDef getBodyDef() {
        return bodyDef;
    }

    public final void setBodyDef(final BodyDef bodyDefToSet) {
        this.bodyDef = bodyDefToSet;
    }

    public final Body getBody() {
        return body;
    }

    public final void setBody(final Body bodyToSet) {
        this.body = bodyToSet;
    }

    public final FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public final void setFixtureDef(final FixtureDef fixtureDefToSet) {
        this.fixtureDef = fixtureDefToSet;
    }
}
