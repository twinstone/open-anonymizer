package openanonymizer.core.storage;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class allows to store any transformation application during run time.
 * For example you could use this class while saving new entities from SQL database
 * into MongoDB for future relations recover.
 * <p>
 * Transformations are stored in {@link java.util.concurrent.ConcurrentMap}, so read
 * and write operations must be thread safe.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class TransformationStorage {

    private static final Logger logger = Logger.getLogger(TransformationStorage.class);
    private static final Map<String, Queue<Pair>> pairStorage = new ConcurrentHashMap<>();

    /**
     * Allows to insert a new value into transformation storage.
     * All parameters must be not null.
     *
     * @param <L>        type for left value
     * @param <R>        type for right value
     * @param source     identifier of collection, table, etc
     * @param leftValue  previous value
     * @param rightValue future value
     */
    public static <L, R> void insertValue(final String source, final L leftValue, final R rightValue) {
        Validate.notNull(source, "Source must be not null.");
        Validate.notNull(leftValue, "Left value must be not null.");
        Validate.notNull(rightValue, "Right value must be not null.");
        Pair<L, R> pair = Pair.of(leftValue, rightValue);
        if (!pairStorage.containsKey(source)) {
            pairStorage.put(source, new ConcurrentLinkedQueue<>());
        }
        pairStorage.get(source).add(pair);
        logger.info(String.format("Added new transformation for source [%s]: %s -> %s", source, leftValue, rightValue));
    }

    /**
     * Allows to find transformation by left value.
     * All parameters must be not null.
     *
     * @param <L>    type for key
     * @param source identifier of collection, table, etc
     * @param left   left value we lookup in transformations list
     * @return {@link Optional}. It is empty when transformation not found.
     */
    public static <L> Optional<Pair> findByLeft(final String source, final L left) {
        Validate.notNull(source, "Source must be not null.");
        Validate.notNull(left, "Left value must be not null.");
        Queue<Pair> list = pairStorage.get(source);
        if (list != null) {
            return list.stream().filter(pair -> pair.getLeft().equals(left)).findFirst();
        } else {
            logger.warn(String.format("Transformation for source [%s] identified with [%s] not found.", source, left));
            return Optional.empty();
        }
    }

    /**
     * Allows to clear all transformations for specific source.
     * All parameters must be not null.
     *
     * @param source identifier of collection, table, etc
     */
    public static void clearSource(final String source) {
        Validate.notNull(source, "Source must be not null.");
        if (pairStorage.containsKey(source)) {
            pairStorage.remove(source);
            logger.info(String.format("Storage for source [%s] was cleaned.", source));
        } else {
            throw new IllegalArgumentException(String.format("Could not find source with name [%s].", source));
        }
    }

    private TransformationStorage() {
        throw new IllegalStateException();
    }
}
