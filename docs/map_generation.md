# Map Generation

Colony builds new worlds through the `MapGenerator` interface. Implementations return a fully initialized `MapState` for the given dimensions.

## MapGenerator

```java
MapState generate(int width, int height);
```

`MapService` invokes the configured generator when no save file exists. The default server configuration uses `ChunkedMapGenerator`.

## ChunkedMapGenerator

`ChunkedMapGenerator` creates the world in 32×32 tile regions called `MapChunkData`. Tiles are generated lazily using a Perlin noise algorithm and stored in their chunk. Basic resources and a starting building are populated in the center of the map.

## Creating a Custom Generator

Custom terrain algorithms can be plugged in by providing a different `MapGenerator` when building the server configuration:

```java
MapGenerator generator = new MyGenerator();
GameServerConfig config = GameServerConfig.builder()
        .mapGenerator(generator)
        .build();
```

`MapService` will call the supplied generator to produce the initial state. This approach keeps map creation separate from the rest of the server logic.

