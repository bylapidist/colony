package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.MapRenderData;

/**
 * Renders building entities.
 */
public final class BuildingRenderer implements EntityRenderer<RenderBuilding> {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final AssetResolver resolver;
    private final java.util.HashMap<String, TextureRegion> buildingRegions = new java.util.HashMap<>();
    private final BitmapFont font = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();
    private static final float LABEL_OFFSET_Y = 8f;
    private final Rectangle viewBounds = new Rectangle();
    private final Vector2 worldCoords = new Vector2();

    public BuildingRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final CameraProvider cameraSystemToSet,
            final AssetResolver resolverToSet
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.cameraSystem = cameraSystemToSet;
        this.resolver = resolverToSet;

        for (String type : new String[]{"HOUSE", "MARKET", "FACTORY", "FARM"}) {
            String ref = resolver.buildingAsset(type);
            TextureRegion region = resourceLoader.findRegion(ref);
            if (region != null) {
                buildingRegions.put(type, region);
            }
        }
    }

    @Override
    public void render(final MapRenderData map) {
        Rectangle view = CameraUtils.getViewBounds(
                (com.badlogic.gdx.graphics.OrthographicCamera) cameraSystem.getCamera(),
                (com.badlogic.gdx.utils.viewport.ExtendViewport) cameraSystem.getViewport(),
                viewBounds
        );
        Array<RenderBuilding> entities = map.getBuildings();
        for (int i = 0; i < entities.size; i++) {
            RenderBuilding building = entities.get(i);
            CameraUtils.tileCoordsToWorldCoords(building.getX(), building.getY(), worldCoords);

            if (!CameraUtils.isVisible(view, worldCoords.x, worldCoords.y)) {
                continue;
            }

            String type = building.getBuildingType();
            TextureRegion region = buildingRegions.get(type);
            if (region != null) {
                spriteBatch.draw(region, worldCoords.x, worldCoords.y);
            }
            if (!resolver.hasBuildingAsset(type)) {
                layout.setText(font, type);
                font.draw(spriteBatch, layout, worldCoords.x, worldCoords.y + LABEL_OFFSET_Y);
            }
        }
    }
}
