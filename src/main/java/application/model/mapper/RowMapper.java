package application.model.mapper;

import application.model.describer.EntityDescriber;
import application.model.wrapper.EntityWrapper;
import application.model.wrapper.EntityWrapperImpl;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RowMapper implements EntityWrapperMapper<String[]> {

    private static final Pattern withQuotes = Pattern.compile("^('|\\\")(.*)('|\\\")$");
    private final Map<String, Integer> indexMapping;

    public RowMapper() {
        indexMapping = Collections.emptyMap();
    }

    public RowMapper(String[] fields, EntityDescriber describer) {
        Validate.notEmpty(fields, "Fields must be not null or empty.");
        Validate.notNull(describer, "Describer must be not null.");
        indexMapping = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (describer.containsField(field)) {
                indexMapping.put(field, i);
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
        return new String[0];
    }

    private String removeQuoting(String input) {
        Matcher inputMatcher = withQuotes.matcher(input);
        if (inputMatcher.matches()) {
            return inputMatcher.group(2);
        }
        return input;
    }
}
