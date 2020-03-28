package application.core.dict;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 */
public final class DictionaryService {

    private static final Logger logger = Logger.getLogger(DictionaryService.class);

    private static final String DELIMITER = ";";
    private static final String FILENAME = "%s/%s.dict";
    private static Map<String, Dictionary> loadedDictionaries = new HashMap<>();

    public static Optional<String> getDictionaryValue(final String path, final String name) {
        return getDictionaryValue(path, name, null);
    }

    public static Optional<String> getDictionaryValue(final String path, final String name, final Locale locale) {
        Validate.notEmpty(path, "Dictionaries path must be not null and not empty.");
        Validate.notEmpty(name, "File name must be not null and not empty.");
        String key = name + "_" + (locale == null ? "def" : locale.getLanguage());
        if (loadedDictionaries.containsKey(key)) {
            return Optional.of(loadedDictionaries.get(key).nextValue());
        }
        Dictionary dictionary = loadDictionary(String.format(FILENAME, path, key));
        loadedDictionaries.put(key, dictionary);
        return Optional.of(dictionary.nextValue());
    }

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

    private static class Dictionary {
        private final Iterable<String> iterable;
        private Iterator<String> iterator;

        Dictionary(Iterable<String> iterable) {
            this.iterable = iterable;
            this.iterator = iterable.iterator();
        }

        String nextValue() {
            if (!iterator.hasNext()) {
                iterator = iterable.iterator();
            }
            return iterator.next();
        }
    }
}
