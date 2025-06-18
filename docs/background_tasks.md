# Background Tasks

Some operations such as map generation and mod initialization can take several seconds.
`MapInitSystem` now supports asynchronous loading when constructed with `async` enabled.
Provide a progress callback to receive updates between `0` and `1` as the map state
is generated. `MapScreen` polls this system during the loading phase and processes
the world once initialization completes.

Use `MapWorldBuilder.builder` with the new callback parameter to create a world
that reports loading progress:

```java
WorldConfigurationBuilder builder = MapWorldBuilder.builder(
        provider,
        client,
        stage,
        keys,
        graphics,
        progress -> loadingScreen.setProgress(progress)
);
```

The default constructors remain synchronous for tests and simple cases.

