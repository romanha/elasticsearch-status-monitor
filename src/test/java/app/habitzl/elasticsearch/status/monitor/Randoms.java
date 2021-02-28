package app.habitzl.elasticsearch.status.monitor;

import java.security.SecureRandom;

/**
 * Utility class for generating random data to be used in tests.
 */
public final class Randoms {

    private static final SecureRandom RANDOM = new SecureRandom();

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
     * Generates a random positive integer value.
     */
    public static int generatePositiveInteger() {
        return generateInteger(0, Integer.MAX_VALUE - 1);
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
        return RANDOM.nextInt(upperLimit + 1) + lowerLimit;
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
}
