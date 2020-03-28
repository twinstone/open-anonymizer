package application.core.dict;

import application.core.random.RandomSequenceGenerator;
import org.apache.commons.lang3.Validate;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public final class MultiDictionaryService {

    private static final Pattern PATTERN = Pattern.compile("[$#]\\{(\\d+)}");

    public static String generate(final String pattern, final String path, final String[] dictionaries, final String[] patterns, final Locale locale) {
        Validate.notEmpty(pattern, "Pattern must be not null or empty.");
        Validate.notEmpty(path, "Path must be not null or empty.");
        int pointer = 0;
        StringBuilder resultBuilder = new StringBuilder();
        Matcher matcher = PATTERN.matcher(pattern);
        while (matcher.find()) {
            resultBuilder.append(pattern, pointer, matcher.start());
            resultBuilder.append(generate(matcher, path, dictionaries, patterns, locale));
            pointer = matcher.end();
        }
        resultBuilder.append(pattern, pointer, pattern.length());
        return resultBuilder.toString();
    }

    private static String generate(final Matcher matcher, final String path, final String[] dictionaries, final String[] patterns, final Locale locale) {
        Integer i = Integer.valueOf(matcher.group(1));
        switch (matcher.group().charAt(0)) {
            case '$':
                Validate.notEmpty(dictionaries, "Dictionaries must be not null or empty.");
                if (dictionaries.length <= i)
                    throw new IllegalArgumentException("Dictionary not defined on position " + i);
                return DictionaryService.getDictionaryValue(path, dictionaries[i], locale).orElse("");
            case '#':
                Validate.notEmpty(patterns, "Patterns must be not null or empty.");
                if (patterns.length <= i) throw new IllegalArgumentException("Pattern not defined on position " + i);
                return RandomSequenceGenerator.generate(patterns[i]);
        }
        throw new IllegalArgumentException("Not specified generation configuration.");
    }

    private MultiDictionaryService() {
        throw new IllegalStateException();
    }
}
