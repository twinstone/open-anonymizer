package openanonymizer.model.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mapper for any row entity saved as {@link String[]}.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class RowMapper implements EntityWrapperMapper<String[]> {

    private static final Pattern withQuotes = Pattern.compile("^('|\\\"|`)(.*)('|\\\"|`)$");
    private final Map<String, Integer> indexMapping;

    public RowMapper(List<String> fields) {
        Validate.notEmpty(fields, "Fields must be not empty.");
        indexMapping = new HashMap<>();
        int counter = 0;
        for (final String field : fields) {
            indexMapping.put(field, counter);
            counter++;
        }
    }

    public RowMapper(String[] fields, EntityDescriber describer) {
        Validate.notEmpty(fields, "Fields must be not null or empty.");
        Validate.notNull(describer, "Describer must be not null.");
        indexMapping = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (describer.containsField(removeQuoting(field))) {
                indexMapping.put(removeQuoting(field), i);
            }
        }
    }

    @Override
    public EntityWrapper getFromEntity(String[] entity, EntityDescriber describer) {
        Validate.notEmpty(entity, "Entity must be not empty array.");
        Validate.notNull(describer, "Describer must be not null.");
        EntityWrapper wrapper = new EntityWrapperImpl(describer);
        try {
            indexMapping.forEach((k, v) -> wrapper.insert(k, removeQuoting(entity[v])));
        } catch (IndexOutOfBoundsException e) {
            throw new MappingException(e);
        }
        return wrapper;
    }

    @Override
    public String[] getFromWrapper(EntityWrapper wrapper) {
        Validate.notNull(wrapper, "Wrapper must be not null.");
        try {
            int size = Collections.max(indexMapping.values()) + 1;
            String[] columns = new String[size];
            indexMapping.forEach((k, v) -> columns[v] = wrapper.getValue(k).toString());
            return columns;
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }

    private String removeQuoting(String input) {
        Matcher inputMatcher = withQuotes.matcher(input);
        if (inputMatcher.matches()) {
            return inputMatcher.group(2);
        }
        return input;
    }
}
