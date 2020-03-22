package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription.SubscriptionListener;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.BitVector;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.components.AssetComponent;
import net.lapidist.colony.core.utils.io.FileLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.artemis.Aspect.all;
import static com.artemis.E.E;

public abstract class AbstractAssetSystem
        extends IteratingSystem implements Disposable {

    private final AssetManager assetManager;
    private final Map<String, Entity> assetsByName;
    private final Map<Entity, String> namesByAsset;
    private final BitVector registered;
    private boolean loaded;
    private FileLocation fileLocation;
    private boolean disposed;

    public AbstractAssetSystem(final FileLocation fileLocationToSet) {
        super(Aspect.all(AssetComponent.class));

        this.assetManager = new AssetManager(fileLocationToSet.getResolver());
        this.assetsByName = new HashMap<>();
        this.namesByAsset = new HashMap<>();
        this.registered = new BitVector();
        this.fileLocation = fileLocationToSet;
    }

    @Override
    protected final void initialize() {
        world.getAspectSubscriptionManager()
                .get(all(AssetComponent.class))
                .addSubscriptionListener(new SubscriptionListener() {
                    @Override
                    public void inserted(final IntBag entities) {
                    }

                    @Override
                    public void removed(final IntBag entities) {
                        deleted(entities);
                    }
                });

        initializeGui();
        initializeMap();
    }

    protected abstract void initializeGui();

    protected abstract void initializeMap();

    private void deleted(final IntBag entities) {
        int[] ids = entities.getData();

        for (int i = 0, s = entities.size(); s > i; i++) {
            int id = ids[i];
            if (registered.get(id)) {

                String removedName = namesByAsset.remove(world.getEntity(id));
                assetsByName.remove(removedName);
                registered.clear(id);

                if (E(id).hasTextureComponent()) {
                    String path = fileLocation.getFile(
                            "textures/" + removedName + ".png"
                    ).path();
                    assetManager.unload(path);
                }
            }
        }
    }

    protected final void register(final String name, final Entity e) {
        unregister(name);

        if (getName(e) != null) {
            unregister(getName(e));
        }

        assetsByName.put(name, e);
        namesByAsset.put(e, name);
        registered.set(e.getId());

        if (E(e).hasTextureComponent()) {
            String path = fileLocation.getFile(
                    "textures/" + name + ".png"
            ).path();

            assetManager.load(path, Texture.class);
        }

        if (E(e).hasFontComponent()) {
            String path = fileLocation.getFile(
                    "fonts/" + name + ".fnt"
            ).path();

            assetManager.load(path, BitmapFont.class);
        }
    }

    protected final void register(final String name, final int entityId) {
        register(name, world.getEntity(entityId));
    }

    protected final void unregister(final String name) {
        Entity removed = assetsByName.remove(name);

        if (removed != null) {
            namesByAsset.remove(removed);
            registered.clear(removed.getId());
        }
    }

    protected final boolean isRegistered(final String name) {
        return assetsByName.containsKey(name);
    }

    protected final Entity getEntity(final String name) {
        return assetsByName.get(name);
    }

    protected final int getEntityId(final String name) {
        Entity e = getEntity(name);
        return e != null ? e.getId() : -1;
    }

    protected final String getName(final Entity e) {
        return namesByAsset.get(e);
    }

    protected final String getName(final int entityId) {
        return getName(world.getEntity(entityId));
    }

    public final Texture getTexture(final String name) {
        if (isRegistered(name)) {
            Entity e = getEntity(name);

            if (E(e).hasTextureComponent()) {
                String path = fileLocation.getFile(
                        "textures/" + name + ".png"
                ).path();
                return assetManager.get(path, Texture.class);
            }
        }

        return null;
    }

    public final BitmapFont getFont(final String name) {
        if (isRegistered(name)) {
            Entity e = getEntity(name);

            if (E(e).hasFontComponent()) {
                String path = fileLocation.getFile(
                        "fonts/" + name + ".fnt"
                ).path();
                return assetManager.get(path, BitmapFont.class);
            }
        }

        return null;
    }

    protected final Collection<String> getRegisteredAssets() {
        return namesByAsset.values();
    }

    @Override
    public final void dispose() {
        if (!disposed) {
            disposed = true;
            assetManager.dispose();
        }
    }

    @Override
    protected final void process(final int entityId) {
        if (!loaded) {
            loaded = assetManager.update();
            return;
        }

        if (E(entityId).hasTextureComponent()) {
            if (E(entityId).getTextureComponent().getTexture() == null) {
                E(entityId).getTextureComponent().setTexture(
                        getTexture(getName(entityId))
                );
            }
        }

        if (E(entityId).hasFontComponent()) {
            if (E(entityId).getFontComponent().getFont() == null) {
                E(entityId).getFontComponent().setFont(
                        getFont(getName(entityId))
                );
            }
        }

        processGui(entityId);
        processMap(entityId);
    }

    protected abstract void processGui(int entityId);

    protected abstract void processMap(int entityId);

    protected final boolean isLoaded() {
        return loaded;
    }
}
