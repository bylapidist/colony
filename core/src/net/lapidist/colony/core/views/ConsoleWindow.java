package net.lapidist.colony.core.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.kotcrab.vis.ui.widget.VisTextArea;
import net.lapidist.colony.core.utils.io.FileLocation;

public class ConsoleWindow extends AbstractLmlView {

    @LmlActor("console")
    private VisTextArea console;

    public ConsoleWindow() {
        super(new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
    }

    public void addLine(String line) {
        console.setText(console.getText() + line + "\n");
    }

    public void roll() {
        addLine(String.valueOf((int) (MathUtils.random() * 1000)));
    }

    @Override
    public FileHandle getTemplateFile() {
        return FileLocation.INTERNAL.getFile("views/console.lml");
    }

    @Override
    public String getViewId() {
        return "console";
    }
}
