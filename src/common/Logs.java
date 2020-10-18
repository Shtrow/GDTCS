
/**
 * Log system
 *
 * @author Marais-Viau
 */

package common;

import java.util.logging.Logger;
import java.util.logging.Level;

public class Logs {
	private static Logger logger = Logger.getLogger("logger");

	public static void log(String message) {
		logger.log(Level.INFO, message);
	}

	public static void warning(String message) {
		logger.log(Level.WARNING, message);
	}

	public static void error(String message) {
		logger.log(Level.SEVERE, message);
	}
}
