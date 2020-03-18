package net.lapidist.colony.client.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import net.lapidist.colony.core.utils.io.FileLocation;

public class InventoryWindow extends AbstractLmlView {

    public InventoryWindow() {
        super(new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
    }

    @Override
    public FileHandle getTemplateFile() {
        return FileLocation.INTERNAL.getFile("views/inventory.lml");
    }

    @Override
    public String getViewId() {
        return "inventory";
    }
}
