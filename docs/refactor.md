# Refactoring Plan

This document identifies classes with large constructors or public APIs and outlines how they can be refactored into smaller, single-responsibility components.

## PlayerCameraSystem
`PlayerCameraSystem` exposes many coordinate conversion helpers that duplicate functionality in `CameraUtils` and are unused outside the class. This inflates the public API.

### Proposed Changes
- Remove unused conversion methods from `PlayerCameraSystem`.
- Expose camera positioning only through `moveCameraToWorldCoords` and the existing getters/setters.
- Use `CameraUtils` directly when conversion utilities are needed.

### Resulting API
```java
public final class PlayerCameraSystem extends BaseSystem {
    public PlayerCameraSystem();
    public void moveCameraToWorldCoords(Vector2 worldCoords);
    public float getZoom();
    public OrthographicCamera getCamera();
    public void setCamera(OrthographicCamera camera);
    public ExtendViewport getViewport();
    public void setViewport(ExtendViewport viewport);
}
```

## InputGestureListener
The constructor of `InputGestureListener` currently accepts five parameters. Grouping related handlers into a context object reduces the number of constructor parameters and clarifies intent.

```java
public record InputHandlers(
    GestureInputHandler gesture,
    KeyboardInputHandler keyboard,
    TileSelectionHandler tileSelection
) {}

public final class InputGestureListener extends GestureAdapter {
    public InputGestureListener(InputHandlers handlers,
                                MapComponent map,
                                ComponentMapper<TileComponent> tileMapper) {
        // ...
    }
}
```

This approach makes the constructor easier to read and emphasises that the handlers work together.

