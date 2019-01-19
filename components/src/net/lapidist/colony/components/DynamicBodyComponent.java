package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class DynamicBodyComponent extends Component {

    private BodyDef bodyDef;
    private Body body;

    public DynamicBodyComponent() {
        this.bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    public DynamicBodyComponent(BodyDef bodyDef) {
        this();

        setBodyDef(bodyDef);
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
}
