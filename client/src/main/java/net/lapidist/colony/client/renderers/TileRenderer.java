package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.TileRotationUtil;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.TileDefinition;

/**
 * Renders tile entities.
 */
public final class TileRenderer implements EntityRenderer<RenderTile> {
    private final net.lapidist.colony.client.network.GameClient client;

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final AssetResolver resolver;
    private final GraphicsSettings graphicsSettings;
    private final java.util.HashMap<String, TextureRegion> tileRegions = new java.util.HashMap<>();
    private final java.util.HashMap<String, TextureRegion> normalRegions = new java.util.HashMap<>();
    private final java.util.HashMap<String, TextureRegion> specularRegions = new java.util.HashMap<>();
    private final TextureRegion overlayRegion;
    private final BitmapFont font = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();
    private static final float LABEL_OFFSET_Y = 8f;
    private final Rectangle viewBounds = new Rectangle();
    private final Vector2 tmpStart = new Vector2();
    private final Vector2 tmpEnd = new Vector2();
    private final Vector2 worldCoords = new Vector2();
    private boolean overlayOnly;

    // CHECKSTYLE:OFF: ParameterNumber
    public TileRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final CameraProvider cameraSystemToSet,
            final AssetResolver resolverToSet,
            final net.lapidist.colony.client.network.GameClient clientToUse,
            final GraphicsSettings graphicsSettingsToUse
    ) {
        // CHECKSTYLE:ON: ParameterNumber
        this.client = clientToUse;
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.cameraSystem = cameraSystemToSet;
        this.resolver = resolverToSet;
        this.graphicsSettings = graphicsSettingsToUse;

        for (TileDefinition def : Registries.tiles().all()) {
            String ref = resolver.tileAsset(def.id());
            TextureRegion region = resourceLoader.findRegion(ref);
            if (region != null) {
                tileRegions.put(def.id().toUpperCase(java.util.Locale.ROOT), region);
            }
            TextureRegion n = resourceLoader.findNormalRegion(ref);
            if (n != null) {
                normalRegions.put(def.id().toUpperCase(java.util.Locale.ROOT), n);
            }
            TextureRegion s = resourceLoader.findSpecularRegion(ref);
            if (s != null) {
                specularRegions.put(def.id().toUpperCase(java.util.Locale.ROOT), s);
            }
        }
        this.overlayRegion = resourceLoader.findRegion("hoveredTile0");
    }


    public void setOverlayOnly(final boolean overlay) {
        this.overlayOnly = overlay;
    }

    @Override
    public void render(final MapRenderData map) {
        Rectangle view = CameraUtils.getViewBounds(
                (com.badlogic.gdx.graphics.OrthographicCamera) cameraSystem.getCamera(),
                (com.badlogic.gdx.utils.viewport.ExtendViewport) cameraSystem.getViewport(),
                viewBounds
        );
        Vector2 start = CameraUtils.worldCoordsToTileCoords((int) view.x, (int) view.y, tmpStart);
        Vector2 end = CameraUtils.worldCoordsToTileCoords(
                (int) (view.x + view.width),
                (int) (view.y + view.height),
                tmpEnd
        );

        int mapWidth = client != null ? client.getMapWidth() : MapState.DEFAULT_WIDTH;
        int mapHeight = client != null ? client.getMapHeight() : MapState.DEFAULT_HEIGHT;

        int startX = Math.max(0, (int) start.x - 1);
        int startY = Math.max(0, (int) start.y - 1);
        int endX = Math.min(mapWidth - 1, (int) end.x + 1);
        int endY = Math.min(mapHeight - 1, (int) end.y + 1);


        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                RenderTile tile = map.getTile(x, y);
                if (tile == null) {
                    continue;
                }

                CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY(), worldCoords);

                if (!overlayOnly) {
                    String type = tile.getTileType();
                    TextureRegion region = tileRegions.get(type.toUpperCase(java.util.Locale.ROOT));
                    if (region != null) {
                        TextureRegion nrm = normalRegions.get(type.toUpperCase(java.util.Locale.ROOT));
                        TextureRegion spec = specularRegions.get(type.toUpperCase(java.util.Locale.ROOT));
                        com.badlogic.gdx.graphics.glutils.ShaderProgram shader = spriteBatch.getShader();
                        if (shader != null) {
                            if (nrm != null && graphicsSettings.isNormalMapsEnabled()) {
                                nrm.getTexture().bind(1);
                                shader.setUniformi("u_normal", 1);
                            }
                            if (spec != null && graphicsSettings.isSpecularMapsEnabled()) {
                                spec.getTexture().bind(2);
                                shader.setUniformi("u_specular", 2);
                            }
                            com.badlogic.gdx.Gdx.gl.glActiveTexture(com.badlogic.gdx.graphics.GL20.GL_TEXTURE0);
                        }
                        String upper = type.toUpperCase(java.util.Locale.ROOT);
                        if ("GRASS".equals(upper) || "DIRT".equals(upper)) {
                            float rotation = TileRotationUtil.rotationFor(tile.getX(), tile.getY());
                            spriteBatch.draw(
                                    region,
                                    worldCoords.x,
                                    worldCoords.y,
                                    region.getRegionWidth() / 2f,
                                    region.getRegionHeight() / 2f,
                                    region.getRegionWidth(),
                                    region.getRegionHeight(),
                                    1f,
                                    1f,
                                    rotation
                            );
                        } else {
                            spriteBatch.draw(region, worldCoords.x, worldCoords.y);
                        }
                    }
                    if (!resolver.hasTileAsset(type)) {
                        layout.setText(font, type);
                        font.draw(spriteBatch, layout, worldCoords.x, worldCoords.y + LABEL_OFFSET_Y);
                    }
                }

                if (tile.isSelected() && overlayRegion != null) {
                    spriteBatch.draw(overlayRegion, worldCoords.x, worldCoords.y);
                }
            }
        }
    }
}
