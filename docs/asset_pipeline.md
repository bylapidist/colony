# Asset Pipeline

Textures are packed using LibGDX's `TexturePacker` into a single `.atlas` file. Each
region may define additional custom fields which are preserved by the atlas loader.

The normal mapping shader reads a `specularPower` value from atlas regions to
control the Blinn–Phong exponent. Add a line like `specularPower: 32` under a
region entry to override the default of `16`.

Run `./gradlew tests:copyAssets` whenever the atlas is updated so the test module
has the latest resources.

