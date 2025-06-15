package net.lapidist.colony.example;

import net.lapidist.colony.mod.GameMod;

/** Simple mod that prints a greeting when loaded. */
public final class HelloWorldMod implements GameMod {
    @Override
    public void init() {
        System.out.println("Hello world from HelloWorldMod");
    }
}

