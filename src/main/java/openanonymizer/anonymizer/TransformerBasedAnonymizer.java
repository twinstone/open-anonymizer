package openanonymizer.anonymizer;

import openanonymizer.core.transformer.DefaultTransformer;
import openanonymizer.core.transformer.Transformer;
import org.apache.commons.lang3.StringUtils;

/**
 * This class declare only one method, that returns new instance of {@link Transformer}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class TransformerBasedAnonymizer {

    private static final String TRANSFORMER = "transformer";

    /**
     * Creates instance of value transformer.
     *
     * @param configuration anonymization process configuration
     * @return instance of {@link Transformer} or {@link DefaultTransformer} if configuration is empty.
     */
    Transformer<?> getTransformer(AnonymizationConfiguration configuration) throws Exception {
        String transformer = String.valueOf(configuration.getParam(TRANSFORMER));
        if (StringUtils.isNotEmpty(transformer)) {
            Class<?> clazz = Class.forName(transformer);
            if (Transformer.class.isAssignableFrom(clazz)) {
                return (Transformer<?>) clazz.newInstance();
            }
        }
        return new DefaultTransformer();
    }

}
