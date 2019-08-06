package net.lapidist.colony.core.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.vis.util.VisLml;

public class ViewRenderer extends LmlApplicationListener {

    @Override
    protected LmlParser createParser() {
        return VisLml.parser()
                .i18nBundle(I18NBundle.createBundle(Gdx.files.internal("i18n/bundle")))
                .build();
    }

    @Override
    public void render() {
        if (getCurrentView() != null) {
            getCurrentView().render(Gdx.graphics.getDeltaTime());
        }
    }
}
