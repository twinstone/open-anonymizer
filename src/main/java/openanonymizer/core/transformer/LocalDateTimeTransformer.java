package openanonymizer.core.transformer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Creates new instance of {@link LocalDateTime} using 'yyyy-MM-dd HH:mm:ss' format.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class LocalDateTimeTransformer implements Transformer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDateTime transform(String input) {
        return LocalDateTime.parse(input, formatter);
    }
}
