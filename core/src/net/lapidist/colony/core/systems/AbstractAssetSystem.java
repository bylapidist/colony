package net.lapidist.colony.core.systems;

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
import net.lapidist.colony.components.assets.AssetComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.render.AssetRegisterEvent;
import net.lapidist.colony.core.utils.io.FileLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.artemis.E.*;
import static com.artemis.Aspect.all;

public abstract class AbstractAssetSystem extends IteratingSystem implements Disposable {

    protected boolean loaded;
    protected final AssetManager assetManager;

    private final Map<String, Entity> assetsByName;
    private final Map<Entity, String> namesByAsset;
    private final BitVector registered;
    private FileLocation fileLocation;
    private boolean disposed;

    public AbstractAssetSystem(FileLocation fileLocation) {
        super(Aspect.all(AssetComponent.class));

        this.assetManager = new AssetManager(fileLocation.getResolver());
        this.assetsByName = new HashMap<>();
        this.namesByAsset = new HashMap<>();
        this.registered = new BitVector();
        this.fileLocation = fileLocation;
    }

    @Override
    protected void initialize() {
        world.getAspectSubscriptionManager()
                .get(all(AssetComponent.class))
                .addSubscriptionListener(new SubscriptionListener() {
                    @Override
                    public void inserted(IntBag entities) { }

                    @Override
                    public void removed(IntBag entities) {
                        deleted(entities);
                    }
                });
    }

    private void deleted(IntBag entities) {
        int[] ids = entities.getData();

        for (int i = 0, s = entities.size(); s > i; i++) {
            int id = ids[i];
            if (registered.get(id)) {

                String removedName = namesByAsset.remove(world.getEntity(id));
                assetsByName.remove(removedName);
                registered.clear(id);

                if (E(id).hasTextureComponent()) {
                    String path = fileLocation.getFile("textures/" + removedName + ".png").path();
                    assetManager.unload(path);
                }
            }
        }
    }

    protected void register(String name, Entity e) {
        unregister(name);

        if (getName(e) != null) {
            unregister(getName(e));
        }

        assetsByName.put(name, e);
        namesByAsset.put(e, name);
        registered.set(e.getId());

        if (E(e).hasTextureComponent()) {
            String path = fileLocation.getFile("textures/" + name + ".png").path();
            Events.fire(new AssetRegisterEvent(path, Texture.class));

            assetManager.load(path, Texture.class);
        }

        if (E(e).hasFontComponent()) {
            String path = fileLocation.getFile("fonts/" + name + ".fnt").path();
            Events.fire(new AssetRegisterEvent(path, BitmapFont.class));

            assetManager.load(path, BitmapFont.class);
        }
    }

    protected void register(String name, int entityId) {
        register(name, world.getEntity(entityId));
    }

    protected void unregister(String name) {
        Entity removed = assetsByName.remove(name);

        if (removed != null) {
            namesByAsset.remove(removed);
            registered.clear(removed.getId());
        }
    }

    protected boolean isRegistered(String name) {
        return assetsByName.containsKey(name);
    }

    protected Entity getEntity(String name) {
        return assetsByName.get(name);
    }

    protected int getEntityId(String name) {
        Entity e = getEntity(name);
        return e != null ? e.getId() : -1;
    }

    protected String getName(Entity e) {
        return namesByAsset.get(e);
    }

    protected String getName(int entityId) {
        return getName(world.getEntity(entityId));
    }

    public Texture getTexture(String name) {
        if (isRegistered(name)) {
            Entity e = getEntity(name);

            if (E(e).hasTextureComponent()) {
                String path = fileLocation.getFile("textures/" + name + ".png").path();
                return assetManager.get(path, Texture.class);
            }
        }

        return null;
    }

    public BitmapFont getFont(String name) {
        if (isRegistered(name)) {
            Entity e = getEntity(name);

            if (E(e).hasFontComponent()) {
                String path = fileLocation.getFile("fonts/" + name + ".fnt").path();
                return assetManager.get(path, BitmapFont.class);
            }
        }

        return null;
    }

    protected Collection<String> getRegisteredAssets() {
        return namesByAsset.values();
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            assetManager.dispose();
        }
    }

    @Override
    protected void process(int entityId) {
        if (!loaded) {
            loaded = assetManager.update();
            return;
        }

        if (E(entityId).hasTextureComponent()) {
            if (E(entityId).getTextureComponent().getTexture() == null) {
                E(entityId).getTextureComponent().setTexture(getTexture(getName(entityId)));
            }
        }

        if (E(entityId).hasFontComponent()) {
            if (E(entityId).getFontComponent().getFont() == null) {
                E(entityId).getFontComponent().setFont(getFont(getName(entityId)));
            }
        }
    }
}
