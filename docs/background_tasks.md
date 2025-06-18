# Background Tasks

Some game systems perform expensive work that would normally stall the main thread.
To keep the UI responsive these operations can run on worker threads and report
progress back to the caller.

`SpriteMapRendererFactory` already exposes a progress callback for asynchronous
texture loading. The same pattern now applies to map generation and mod
initialisation.

## Map generation

`MapInitSystem` accepts an optional progress callback. When provided, the system
spawns a worker thread to generate the `MapState`. Tile and entity creation then
reports incremental progress until the map is ready.

```java
MapInitSystem init = new MapInitSystem(provider, progress -> loading.setProgress(progress));
```

The world continues to process each frame while the map loads in the background.

## Mod initialisation

`ModInitTask` loads all mods discovered by `ModLoader` on a background thread and
invokes each mod's `init()` hook. Callers may supply a progress consumer to
update a loading screen:

```java
ModInitTask task = new ModInitTask(new ModLoader(Paths.get()), loading::setProgress);
CompletableFuture<List<LoadedMod>> mods = CompletableFuture.supplyAsync(task);
```

Once the future completes the returned list contains the loaded mods ready for use.
