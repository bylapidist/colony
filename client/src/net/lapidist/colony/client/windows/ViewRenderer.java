package net.lapidist.colony.client.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.vis.util.VisLml;

public class ViewRenderer extends LmlApplicationListener {

    @Override
    protected final LmlParser createParser() {
        return VisLml.parser()
                .i18nBundle(I18NBundle.createBundle(
                        Gdx.files.internal("i18n/bundle")
                ))
                .build();
    }

    @Override
    public final void render() {
        if (getCurrentView() != null) {
            getCurrentView().render(Gdx.graphics.getDeltaTime());
        }
    }
}
