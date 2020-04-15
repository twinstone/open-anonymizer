package openanonymizer.core.dict;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class allows to get value from dictionary file.
 * Dictionary files, that are represented by {@link Dictionary} class,
 * are loaded into memory, for faster value generation.
 *
 * Dictionary files could loaded from any storage, but must follow next format: name_locale.dict,
 * where 'name' is a dictionary name, 'locale' is a dictionary locale and 'dict' is a file format.
 * Files could be normal text files, with content separated by new line. For separating content in
 * single line use delimiter ';'.
 *
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class DictionaryService {

    private static final Logger logger = Logger.getLogger(DictionaryService.class);

    private static final String DELIMITER = ";";
    private static final String FILENAME = "%s/%s.dict";
    private static Map<String, Dictionary> loadedDictionaries = new HashMap<>();

    /**
     * Get dictionary value using default locale
     *
     * @param path to the dictionaries directory
     * @param name of the dictionary file
     * @return {@link Optional} with dictionary value
     */
    public static Optional<String> getDictionaryValue(final String path, final String name) {
        return getDictionaryValue(path, name, null);
    }

    /**
     * Get dictionary value using specific locale.
     * If locale is set to null, dictionary file with 'def' ending will be used.
     *
     * @param path   to the dictionaries directory
     * @param name   of the dictionary file
     * @param locale dictionary locale
     * @return {@link Optional} with dictionary value
     */
    public static Optional<String> getDictionaryValue(final String path, final String name, final Locale locale) {
        Validate.notEmpty(path, "Dictionaries path must be not null and not empty.");
        Validate.notEmpty(name, "File name must be not null and not empty.");
        String key = name + "_" + (locale == null ? "def" : locale.getLanguage());
        if (loadedDictionaries.containsKey(key)) {
            return Optional.ofNullable(loadedDictionaries.get(key).nextValue());
        }
        Dictionary dictionary = loadDictionary(String.format(FILENAME, path, key));
        loadedDictionaries.put(key, dictionary);
        return Optional.ofNullable(dictionary.nextValue());
    }

    /**
     * Load dictionary file into memory
     *
     * @param filename name of file in local storage
     * @return {@link Dictionary} class with loaded content from dictionary file
     */
    private static Dictionary loadDictionary(final String filename) {
        logger.info("Loading new dictionary from file [ " + filename + " ].");
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<String> wordList = new ArrayList<>();
            reader.lines().forEach(line -> wordList.addAll(Arrays.asList(line.split(DELIMITER))));
            logger.info("Loaded " + wordList.size() + " new terms.");
            return new Dictionary(wordList);
        } catch (IOException e) {
            logger.warn("Could not load dictionary from file [ " + filename + " ]. Dictionary will be empty." );
            return new Dictionary(Collections.emptyList());
        }
    }

    private DictionaryService() {
        throw new IllegalStateException();
    }

    /**
     * This class is a container for dictionary values. It allows reading values in cycle.
     */
    private static class Dictionary {
        private final List<String> iterable;
        private Iterator<String> iterator;

        Dictionary(List<String> iterable) {
            this.iterable = iterable;
            this.iterator = iterable.iterator();
        }

        /**
         * Return next value from values collection.
         * When iterator points to last element of collection,
         * it is set to the collection first element.
         */
        String nextValue() {
            if (iterable.isEmpty()) {
                return null;
            } else if (!iterator.hasNext()) {
                iterator = iterable.iterator();
            }
            return iterator.next();
        }
    }
}
