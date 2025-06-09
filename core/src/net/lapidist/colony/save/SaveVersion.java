package net.lapidist.colony.save;

/**
 * Enumerates supported save game versions.
 */
public enum SaveVersion {
    V1(1),
    V2(2),
    V3(3),
    V4(4),
    V5(5);

    public static final SaveVersion CURRENT = V5;

    private final int number;

    SaveVersion(final int versionNumber) {
        this.number = versionNumber;
    }

    /**
     * Numeric representation used in save files.
     */
    public int number() {
        return number;
    }

    /**
     * Resolves a {@link SaveVersion} from the given number.
     *
     * @param number the version number
     * @return the corresponding enum value
     */
    public static SaveVersion fromNumber(final int number) {
        for (SaveVersion v : values()) {
            if (v.number == number) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown save version: " + number);
    }
}
