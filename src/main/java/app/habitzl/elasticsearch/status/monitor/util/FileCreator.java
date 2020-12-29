package app.habitzl.elasticsearch.status.monitor.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for creating files.
 */
public class FileCreator {

	/**
	 * Creates the directory or file based on the path.
	 * It also creates all the required parent directories.
	 *
	 * @return the path to created file.
	 */
	public Path create(final Path path) throws IOException {
		return Files.createDirectories(path);
	}
}
