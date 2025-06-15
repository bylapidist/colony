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
`settings.conf` or selected through the graphics settings screen.

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

## Built-in lighting

The engine ships with two lighting plugins powered by
[box2d-lights](https://github.com/libgdx/box2dlights):

* `Box2dLightsPlugin` – exposes a `RayHandler` but uses the default sprite shader.
* `LightsNormalMapShaderPlugin` – combines the normal mapping shader with the same `RayHandler`.

The second plugin is now selected by default through the
`graphics.shaderPlugin` setting. Lighting will be skipped automatically when
frame buffers are unavailable such as in headless tests.

The fragment shader used by `LightsNormalMapShaderPlugin` defines two
additional uniforms:

* `u_lightDir` – normalized direction to the main light source.
* `u_viewDir` – direction toward the camera.
* `u_specularPower` – exponent for the specular highlight.

The first two values are updated every frame so diffuse and specular terms react
to camera movement. The specular map supplies the intensity for a Blinn–Phong
highlight calculation, while `u_specularPower` controls the falloff. Atlas
regions may override the default by adding `specularPower: N`.
