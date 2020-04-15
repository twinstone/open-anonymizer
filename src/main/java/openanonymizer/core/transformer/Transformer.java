package openanonymizer.core.transformer;

/**
 * An instance of this interface should allow converting {@link String} value into new instance of class T.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public interface Transformer<T> {

    /**
     * Convert input into new instance of class T.
     *
     * @param input value
     * @return T
     */
    T transform(final String input);

}
