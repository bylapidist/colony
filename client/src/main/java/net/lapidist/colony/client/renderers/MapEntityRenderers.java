package net.lapidist.colony.client.renderers;

/** Holds auxiliary entity renderers used by the map renderer. */
public record MapEntityRenderers(
        ResourceRenderer resourceRenderer,
        PlayerRenderer playerRenderer,
        CelestialRenderer celestialRenderer
) {
}
