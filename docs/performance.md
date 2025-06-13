# Performance Notes

This document records benchmarks around common data structures used in the project.

## GameClient message queues

`GameClient` uses `ConcurrentHashMap` and `ConcurrentLinkedQueue` to store incoming
network messages. A simple microbenchmark was run to estimate the cost of these
thread-safe structures compared to their non-synchronized counterparts. Each test
performed one million put/get or add/poll operations on JDK 21.

| Structure | Time (ms) |
|-----------|-----------|
| ConcurrentHashMap | ~190 |
| HashMap | ~80 |
| ConcurrentLinkedQueue | ~53 |
| ArrayDeque | ~47 |

`ConcurrentHashMap` showed roughly 2× the overhead of `HashMap` while
`ConcurrentLinkedQueue` was only about 13% slower than `ArrayDeque`.

`GameClient` receives messages on the networking thread while the main game loop
polls them concurrently. Because of this cross‑thread access the concurrent
collections remain necessary despite the overhead.

## Map sprite caching

`SpriteBatchMapRenderer` can store tiles in `SpriteCache` objects to avoid
per-frame lookups. The cache is rebuilt whenever the map data changes and can be
disabled via the `graphics.spritecache` setting. Benchmarks show large maps
render about 20% faster with caching enabled.

## Asynchronous renderer loading

`SpriteMapRendererFactory` now loads assets using LibGDX's `AssetManager` and
returns a lightweight renderer that simply
poll the asset manager each frame and draw nothing until all resources are
ready. The optional progress callback reports loading progress between 0 and 1
so callers can update a loading screen while the world continues to run.

## Microbenchmarks

Additional performance tests use JMH and reside in the `tests` module. Run them with:

```bash
./gradlew :tests:jmh
```

To execute a specific benchmark use the `jmh.include` property:

```bash
./gradlew :tests:jmh -Djmh.include=SpriteBatchRendererBenchmark
```

## Rendering pipeline benchmark

`SpriteBatchRendererBenchmark` measures the render system with and without tile
caching enabled. Benchmarks start a headless LibGDX application automatically so
they can run without a display.

### Benchmark results (JDK 21)

| Benchmark | Score (ops/s) |
|-----------|---------------|
| MapTileCacheBenchmark.rebuildCache | ~5.9 |
| MapTileCacheBenchmark.updateTile | ~10.8 |
| MapRenderDataSystemBenchmark.updateIncremental (30) | ~15,194,000 |
| MapRenderDataSystemBenchmark.updateIncremental (60) | ~15,476,000 |
| MapRenderDataSystemBenchmark.updateIncremental (90) | ~15,497,000 |
| SpriteBatchRendererBenchmark.renderWithCache | ~12,800 |
| SpriteBatchRendererBenchmark.renderWithoutCache | ~160 |

These results were captured on a headless JDK 21 runtime and serve as a baseline
for future renderer changes.
When rerunning these benchmarks, record any updated scores here. If your new
baseline is higher or lower, update the table so the current performance
expectations remain accurate. Avoid shipping slower results without a strong
reason.

## Network service benchmark

`NetworkServiceBenchmark` measures the cost of sending the initial map state to a
single connection. The benchmark invokes the private `sendMapState` method via
reflection with a 64×64 tile map.

### Benchmark results (JDK 21)

| Benchmark | Score (ops/s) |
|-----------|---------------|
| NetworkServiceBenchmark.sendMapState | ~5,100 |

## Minimap cache performance

`MinimapCacheBenchmark` measures the cost of regenerating the Pixmap-based minimap texture on a 100×100 tile map.

### Benchmark results (JDK 21)

| Benchmark | Score (ops/s) |
|-----------|---------------|
| MinimapCacheBenchmark.buildMinimap | ~2,800 |
