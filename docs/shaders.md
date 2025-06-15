# Shader Plugins

Colony supports pluggable GLSL programs so experiments can be developed
outside the main repository. Plugins implement the small
`ShaderPlugin` interface from the client module:

```java
public interface ShaderPlugin {
    ShaderProgram create(ShaderManager manager);
    default void dispose() { }
}
```

`create` receives a `ShaderManager` which helps compile a vertex and
fragment shader. The method may return `null` when the program cannot be
loaded, for example if the required sources are missing.

## External shader folder

Plugins normally load GLSL sources from the `shaders/` directory inside
the game's external data folder (typically `~/.colony`). Place your
`*.vert` and `*.frag` files in this directory so they can be read with
`FileLocation.EXTERNAL`.

## Service registration

Implementations are discovered through Java's `ServiceLoader`. Each
plugin must be listed in
`META-INF/services/net.lapidist.colony.client.graphics.ShaderPlugin` as
a single fully qualified class name. The default build already contains
an entry for `NullShaderPlugin`.

## Selecting a plugin

The chosen shader is stored in the `graphics.shaderPlugin` setting. It
holds the class name from the service file. This value can be edited in
`settings.properties` or selected through the graphics settings screen.

## Example

A minimal grayscale plugin loading `gray.vert` and `gray.frag` from the
external shader folder could look like:

```java
public final class GrayShaderPlugin implements ShaderPlugin {
    @Override
    public ShaderProgram create(ShaderManager manager) {
        try {
            return manager.load(FileLocation.EXTERNAL,
                    "shaders/gray.vert", "shaders/gray.frag");
        } catch (IOException e) {
            return null;
        }
    }
}
```

Add `GrayShaderPlugin` to the service descriptor and set
`graphics.shaderPlugin` to `com.example.GrayShaderPlugin` to enable it.

## Post processors

Rendering passes can be chained by implementing the `PostProcessor`
interface in the client module. A processor receives the scene via a
framebuffer and may apply additional effects before drawing the final
texture to the back buffer. The provided `BloomPostProcessor` performs
a simple blur over the captured frame. Enable post processing with the
`graphics.post` setting or from the graphics settings screen.
