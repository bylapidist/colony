package net.lapidist.colony.save;

/**
 * Enumerates supported save game versions.
 */
public enum SaveVersion {
    V1(1),
    V2(2),
    V3(3),
    V4(4),
    V5(5),
    V6(6),
    V7(7),
    V8(8),
    V9(9),
    V10(10),
    V11(11),
    V12(12),
    V13(13),
    V14(14),
    V15(15),
    V16(16),
    V17(17),
    V18(18),
    V19(19),
    V20(20),
    V21(21),
    V22(22),
    V23(23),
    V24(24),
    V25(25),
    V26(26),
    V27(27),
    V28(28);

    public static final SaveVersion CURRENT = V28;

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
