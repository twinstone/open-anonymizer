package openanonymizer.core.transformer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Creates new instance of {@link LocalDate} using 'yyyy-MM-dd' format.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class LocalDateTransformer implements Transformer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate transform(String input) {
        return LocalDate.parse(input, formatter);
    }
}
