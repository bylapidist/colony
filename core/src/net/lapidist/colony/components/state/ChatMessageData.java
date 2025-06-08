package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

@KryoType
public record ChatMessageData(String player, String message) {
}
