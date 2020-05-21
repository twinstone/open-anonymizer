package openanonymizer.core.random;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class allows to generate random alphabetic, number or ascii sequences.
 * Sequences can be combined. It could be useful for generating passport id,
 * for example. Pattern will look like 'W{2-2} D{5-5}'. Possible output: AA 11234
 * Generating pattern specification.
 * First char specify what type of sequence will be generated:
 * 'a' - random Ascii output
 * 'd' - random number
 * 'D' - random numeric output
 * 'w' - lower case alphabetic output
 * 'W' - upper case alphabetic output
 * Two numbers in curly brackets specify generation interval lower and upper limits.
 *
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class RandomSequenceGenerator {

    private static final Pattern SEQUENCE_CONFIG = Pattern.compile("[adDwW]\\{(\\d+)-(\\d+)}");
    private static final Map<String, Set<String>> storage = new ConcurrentHashMap<>();

    /**
     * Generates random values.
     *
     * @param pattern for generating
     * @return generated value
     */
    public static String generate(final String pattern) {
        Validate.notEmpty(pattern, "Pattern must be not null or empty.");
        int pointer = 0;
        StringBuilder resultBuilder = new StringBuilder();
        Matcher matcher = SEQUENCE_CONFIG.matcher(pattern);
        while (matcher.find()) {
            resultBuilder.append(pattern, pointer, matcher.start());
            resultBuilder.append(generate(matcher.group().charAt(0),
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2))));
            pointer = matcher.end();
        }
        resultBuilder.append(pattern, pointer, pattern.length());
        return resultBuilder.toString();
    }

    /**
     * Generates random unique values. Previously generated values are stored in memory.
     * Method could cause {@link StackOverflowError}
     *
     * @param pattern for generating
     * @return generated value
     * */
    public static String generateUnique(final String pattern) {
        String result = generate(pattern);
        if (storage.containsKey(pattern)) {
            Set<String> memoryStore = storage.get(pattern);
            while (memoryStore.contains(result)) {
                result = generate(pattern);
            }
            memoryStore.add(result);
        } else {
            storage.put(pattern, Sets.newConcurrentHashSet(Collections.singletonList(result)));
        }
        return result;
    }

    /**
     * Generates random sequence.
     *
     * @param c sequence specification
     * @param from minimal value length/value
     * @param to maximal value length/value
     * */
    private static String generate(char c, final Integer from, final Integer to) {
        Validate.notNull(from, "From must be not null.");
        Validate.notNull(to, "To must be not null.");
        switch (c) {
            case 'd':
                return String.valueOf(RandomUtils.nextInt(from, to));
            case 'D':
                return RandomStringUtils.randomNumeric(from, to);
            case 'a':
                return RandomStringUtils.randomAscii(from, to);
            case 'w':
                return RandomStringUtils.randomAlphabetic(from, to).toLowerCase();
            case 'W':
                return RandomStringUtils.randomAlphabetic(from, to).toUpperCase();
        }
        throw new IllegalArgumentException("Not specified generation configuration.");
    }

    private RandomSequenceGenerator() {
        throw new IllegalStateException();
    }
}
