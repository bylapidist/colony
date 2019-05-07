package net.lapidist.colony.core.views;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.vis.util.VisLml;

public class ViewController extends LmlApplicationListener {

    @Override
    protected LmlParser createParser() {
        return VisLml.parser()
                .build();
    }

    @Override
    public void render() {
        if (getCurrentView() != null) {
//            getCurrentView().render(Gdx.graphics.getDeltaTime());
        }
    }
}
