package openanonymizer.core.anonymizer;

import openanonymizer.core.random.RandomSequenceGenerator;
import openanonymizer.core.transformer.Transformer;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;

/**
 * Pattern based anonymizer. Replaces field value with randomly generated value.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 * */
public class PatternBasedAnonymizer extends TransformerBasedAnonymizer implements Anonymizer {

    private static final String PATTERN = "pattern";
    private static final String UNIQUE = "unique";

    @Override
    public void anonymize(EntityWrapper wrapper, FieldDescriber describer, Configuration configuration) throws Exception {
        Validate.notNull(configuration.getParam(PATTERN), "Configuration param pattern must be not empty.");
        Validate.notNull(configuration.getParam(UNIQUE), "Configuration param unique must be not empty.");
        final String pattern = String.valueOf(configuration.getParam(PATTERN));
        Transformer transformer = getTransformer(configuration);
        if ((Boolean) configuration.getParam(UNIQUE)) {
            wrapper.update(describer.getName(), transformer.transform(RandomSequenceGenerator.generateUnique(pattern)));
        } else {
            wrapper.update(describer.getName(), transformer.transform(RandomSequenceGenerator.generate(pattern)));
        }
    }
}
