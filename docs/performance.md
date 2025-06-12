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
