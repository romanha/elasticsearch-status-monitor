package app.habitzl.elasticsearch.status.monitor;

/**
 * This is the entry point of the program.
 */
public class Main {

    public static void main(final String[] args) {
        int exitCode = Bootstrapper.start(args);
        System.exit(exitCode);
    }
}
