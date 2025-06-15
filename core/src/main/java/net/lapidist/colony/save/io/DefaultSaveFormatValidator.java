package net.lapidist.colony.save.io;

import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.serialization.SerializationRegistrar;

import java.io.IOException;

/**
 * Default implementation checking version and registration hash.
 */
public final class DefaultSaveFormatValidator implements SaveFormatValidator {
    @Override
    public void validate(final SaveData data) throws IOException {
        if (data.version() > SaveVersion.CURRENT.number()) {
            throw new IOException(String.format(
                    "Unsupported map version %d (current %d)",
                    data.version(),
                    SaveVersion.CURRENT.number()
            ));
        }
        if (data.kryoHash() != SerializationRegistrar.registrationHash()) {
            throw new IOException("Save file format mismatch with current Kryo registration");
        }
    }
}
