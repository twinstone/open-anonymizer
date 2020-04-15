package openanonymizer.core.transformer;

/**
 * Do nothing with input.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class DefaultTransformer implements Transformer<String> {
    @Override
    public String transform(String input) {
        return input;
    }
}
