package openanonymizer.anonymizer;

/**
 * An exception class. Could be thrown if anonymization process on some field failed.
 */
public class AnonymizationException extends Exception {
    AnonymizationException(Throwable cause) {
        super(cause);
    }
}
