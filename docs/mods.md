# Modding Guide

Colony supports simple mods discovered in the `mods/` directory next to your saves. Each mod provides a `GameMod` implementation packaged as either a JAR or directory. The `ModLoader` will instantiate every implementation found and invoke its lifecycle hooks when the game starts.

Refer to the in‐code `GameMod` interface for available callbacks and examples in the `tests` module.
