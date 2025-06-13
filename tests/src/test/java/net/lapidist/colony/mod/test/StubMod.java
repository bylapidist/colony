package net.lapidist.colony.mod.test;

import net.lapidist.colony.mod.GameMod;

/** Simple test mod used for ModLoader tests. */
public final class StubMod implements GameMod {
    @Override
    public void init() {
        System.setProperty("stubmod.init", "true");
    }

    @Override
    public void dispose() {
        System.setProperty("stubmod.dispose", "true");
    }
}
