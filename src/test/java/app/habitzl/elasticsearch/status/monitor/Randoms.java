package app.habitzl.elasticsearch.status.monitor;

import java.security.SecureRandom;

/**
 * Utility class for generating random data to be used in tests.
 */
public final class Randoms {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_STRING_LENGTH = 5;
    private static final int UNICODE_INDEX_ZERO = 48;
    private static final int UNICODE_INDEX_NINE = 57;
    private static final int UNICODE_INDEX_CAPITAL_A = 65;
    private static final int UNICODE_INDEX_CAPITAL_Z = 90;
    private static final int UNICODE_INDEX_SMALL_A = 97;
    private static final int UNICODE_INDEX_SMALL_Z = 122;

    private Randoms() {
        // instantiation protection
    }

    /**
     * Generates a random boolean value.
     */
    public static boolean generateBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * Generates a random integer value.
     */
    public static int generateInteger() {
        return RANDOM.nextInt();
    }

    /**
     * Generates a random positive integer value (excluding 0).
     */
    public static int generatePositiveInteger() {
        return generateInteger(1, Integer.MAX_VALUE - 1);
    }

    /**
     * Generates a random integer value from 0 (included) to the given upper limit (included).
     */
    public static int generateInteger(final int upperLimit) {
        return generateInteger(0, upperLimit);
    }

    /**
     * Generates a random integer value from the given lower limit (included) to the given upper limit (included).
     */
    public static int generateInteger(final int lowerLimit, final int upperLimit) {
        return RANDOM.nextInt((upperLimit - lowerLimit) + 1) + lowerLimit;
    }

    /**
     * Generates a random float value.
     */
    public static float generateFloat() {
        return RANDOM.nextFloat();
    }

    /**
     * Returns a random enum value from the given enum.
     */
    public static <T extends Enum<T>> T generateEnumValue(final Class<T> clazz) {
        T[] enumConstants = clazz.getEnumConstants();
        int randomIndex = RANDOM.nextInt(enumConstants.length);
        return enumConstants[randomIndex];
    }

    /**
     * Generates a random alphanumeric String value with 5 characters.
     */
    public static String generateString() {
        return generateString(DEFAULT_STRING_LENGTH);
    }

    /**
     * Appends a random alphanumeric String value with 5 characters to the given prefix.
     */
    public static String generateString(final String prefix) {
        return prefix + generateString(DEFAULT_STRING_LENGTH);
    }

    /**
     * Generates a random alphanumeric String value of the given length.
     * <p>
     * The generated String can contain any character from 0-9, A-Z and a-z.
     */
    private static String generateString(final int length) {
        return RANDOM.ints(UNICODE_INDEX_ZERO, UNICODE_INDEX_SMALL_Z + 1)
                     .filter(unicodeIndex ->
                             (unicodeIndex <= UNICODE_INDEX_NINE || unicodeIndex >= UNICODE_INDEX_CAPITAL_A)
                                     && (unicodeIndex <= UNICODE_INDEX_CAPITAL_Z || unicodeIndex >= UNICODE_INDEX_SMALL_A))
                     .limit(length)
                     .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                     .toString();
    }
}
