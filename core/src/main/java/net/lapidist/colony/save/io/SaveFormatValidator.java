package net.lapidist.colony.save.io;

import net.lapidist.colony.save.SaveData;
import java.io.IOException;

/**
 * Validates save file metadata before loading.
 */
public interface SaveFormatValidator {
    void validate(SaveData data) throws IOException;
}
