# Background Tasks

Some operations such as map generation and mod initialization can take several seconds.
`MapInitSystem` now supports asynchronous loading when constructed with `async` enabled.
Provide a progress callback to receive updates between `0` and `1` as the map state
is generated. `MapScreen` polls this system during the loading phase and processes
the world once initialization completes.

Use `LogicWorldBuilder.builder` with the new callback parameter to create the
logic world and `MapWorldBuilder.builder` for the render world. Both report
loading progress:

```java
WorldConfigurationBuilder builder = LogicWorldBuilder.builder(
        provider,
        client,
        keys,
        progress -> loadingScreen.setProgress(progress)
);
WorldConfigurationBuilder render = MapWorldBuilder.builder(
        provider,
        client,
        stage,
        keys,
        graphics,
        progress -> loadingScreen.setProgress(progress)
);
```

The default constructors remain synchronous for tests and simple cases.

