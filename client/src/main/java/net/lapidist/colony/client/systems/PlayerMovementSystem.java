package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;

/** Handles player movement when player camera mode is active. */
public final class PlayerMovementSystem extends BaseSystem {
    private final net.lapidist.colony.client.network.GameClient client;
    private static final float SPEED = 400f; // units per second

    private final KeyBindings keyBindings;
    private PlayerCameraSystem cameraSystem;
    private ComponentMapper<PlayerComponent> playerMapper;
    private Entity player;
    private int lastTileX;
    private int lastTileY;

    public PlayerMovementSystem(final KeyBindings bindings) {
        this(null, bindings);
    }

    public PlayerMovementSystem(
            final net.lapidist.colony.client.network.GameClient clientToUse,
            final KeyBindings bindings
    ) {
        this.client = clientToUse;
        this.keyBindings = bindings;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        playerMapper = world.getMapper(PlayerComponent.class);
        var players = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(PlayerComponent.class))
                .getEntities();
        if (players.size() > 0) {
            player = world.getEntity(players.get(0));
            PlayerComponent pc = playerMapper.get(player);
            lastTileX = Math.floorDiv((int) pc.getX(), GameConstants.TILE_SIZE);
            lastTileY = Math.floorDiv((int) pc.getY(), GameConstants.TILE_SIZE);
        }
    }

    @Override
    protected void processSystem() {
        if (!cameraSystem.isPlayerMode()) {
            return;
        }
        if (player == null) {
            var players = world.getAspectSubscriptionManager()
                    .get(com.artemis.Aspect.all(PlayerComponent.class))
                    .getEntities();
            if (players.size() > 0) {
                player = world.getEntity(players.get(0));
                PlayerComponent pc = playerMapper.get(player);
                lastTileX = Math.floorDiv((int) pc.getX(), GameConstants.TILE_SIZE);
                lastTileY = Math.floorDiv((int) pc.getY(), GameConstants.TILE_SIZE);
            } else {
                return;
            }
        }
        PlayerComponent pc = playerMapper.get(player);
        float move = SPEED * world.getDelta();
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_UP))) {
            pc.setY(pc.getY() + move);
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_DOWN))) {
            pc.setY(pc.getY() - move);
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_LEFT))) {
            pc.setX(pc.getX() - move);
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_RIGHT))) {
            pc.setX(pc.getX() + move);
        }
        clampPosition(pc);

        int tileX = Math.floorDiv((int) pc.getX(), GameConstants.TILE_SIZE);
        int tileY = Math.floorDiv((int) pc.getY(), GameConstants.TILE_SIZE);
        if (client != null && (tileX != lastTileX || tileY != lastTileY)) {
            lastTileX = tileX;
            lastTileY = tileY;
            client.sendPlayerPositionUpdate(
                    new net.lapidist.colony.components.state.messages.PlayerPositionUpdate(tileX, tileY)
            );
        }
    }

    private void clampPosition(final PlayerComponent pc) {
        float mapWidth = client != null ? client.getMapWidth() : MapState.DEFAULT_WIDTH;
        float mapHeight = client != null ? client.getMapHeight() : MapState.DEFAULT_HEIGHT;
        float maxX = mapWidth * GameConstants.TILE_SIZE;
        float maxY = mapHeight * GameConstants.TILE_SIZE;
        pc.setX(MathUtils.clamp(pc.getX(), 0, maxX));
        pc.setY(MathUtils.clamp(pc.getY(), 0, maxY));
    }
}
