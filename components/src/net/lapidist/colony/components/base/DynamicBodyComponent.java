package net.lapidist.colony.components.base;

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

    public DynamicBodyComponent(BodyDef bodyDef, FixtureDef fixtureDef) {
        this();

        setBodyDef(bodyDef);
        setFixtureDef(fixtureDef);
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }
}
