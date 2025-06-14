# Modding Overview

This guide covers how to organize your `mods/` folder and how `ModLoader` discovers mod implementations.

## Folder layout

All mods live under the `mods/` directory inside the game folder.
Each entry can be either a JAR file or a directory with the following structure:

```
mods/
  example.jar
  othermod/
    mod.json
    META-INF/
      services/
        net.lapidist.colony.mod.GameMod
```

Every directory must contain a `mod.json` file and the standard
`META-INF/services` descriptor listing the implementing class of `GameMod`.
JAR files embed these files within the archive.

A minimal `mod.json` looks like:

```json
{
  "id": "example",
  "version": "1.0.0",
  "dependencies": []
}
```

`id` is the unique identifier used for dependency resolution.
`version` is an arbitrary string.
`dependencies` lists other mod IDs that must be loaded first.

## How mods are discovered

`ModLoader` scans the `mods/` folder and creates a `URLClassLoader` for every
JAR or directory it finds. Each class loader is passed to Java's `ServiceLoader`
to locate implementations of the `GameMod` interface. If a `mod.json` is
missing, the entry is skipped.

