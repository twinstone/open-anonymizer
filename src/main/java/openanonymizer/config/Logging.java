package openanonymizer.config;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class set up {@link org.apache.log4j.LogManager} through xml config file.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class Logging {

    /**
     * Set up logging
     *
     * @param fileName path to configuration file
     */
    public static void configure(final String fileName) {
        DOMConfigurator.configure(fileName);
    }

    private Logging() {
        throw new IllegalStateException();
    }
}
