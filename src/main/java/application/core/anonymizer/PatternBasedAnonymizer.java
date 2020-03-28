package application.core.anonymizer;

import application.core.Configuration;
import application.core.random.RandomSequenceGenerator;
import application.model.describer.FieldDescriber;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;

/**
 *
 */
public class PatternBasedAnonymizer extends AbstractFieldAnonymizer implements Anonymizer {

    private static final String PATTERN = "pattern";
    private static final String UNIQUE = "unique";

    @Override
    protected void resolveRandomStrategy(EntityWrapper entity, FieldDescriber describer, Configuration configuration) {
        Validate.notNull(configuration.getParam(PATTERN), "Configuration param pattern must be not empty.");
        Validate.notNull(configuration.getParam(UNIQUE), "Configuration param unique must be not empty.");
        final String pattern = String.valueOf(configuration.getParam(PATTERN));
        if ((Boolean) configuration.getParam(UNIQUE)) {
            entity.update(describer.getName(), RandomSequenceGenerator.generateUnique(pattern));
        } else {
            entity.update(describer.getName(), RandomSequenceGenerator.generate(pattern));
        }
    }
}
