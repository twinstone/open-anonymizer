package openanonymizer.core.dict;

import openanonymizer.core.random.RandomSequenceGenerator;
import org.apache.commons.lang3.Validate;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class allows to generate random values using multiple dictionary configuration
 * and multiple random value configuration.
 *
 * Could be useful for example for generating random address. See generate method documentation.
 *
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class MultiDictionaryService {

    private static final Pattern PATTERN = Pattern.compile("[$#]\\{(\\d+)}");

    /**
     * Generates random value based on multiple dictionary or random sequence configuration.
     * Simple example is generating address. Pattern will look like '${0} #{0}, #{1} ${1}',
     * where first and last parameter, marked with symbol '$' are dictionary values.
     * Second and third parameter, marked with symbol '#', are randomly generated values.
     * Number between curly brackets define index of pattern / dictionary name that will
     * be used for value generation.
     *
     * @param pattern      for generating
     * @param path         path to dictionaries directory
     * @param dictionaries array of dictionaries that will be used in pattern
     * @param patterns     array of patterns that will be used in pattern
     * @param locale       dictionaries locale
     * @return random generated value
     */
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

    /**
     * Generates random value based on matched pattern.
     *
     * @param matcher for generation pattern
     * @param path path to dictionaries directory
     * @param dictionaries array of dictionaries that will be used in pattern
     * @param patterns array of patterns that will be used in pattern
     * @param locale dictionaries locale
     * @return random generated value
     * */
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
