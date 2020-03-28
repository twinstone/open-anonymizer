package application.core.storage;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class TransformationStorage {

    private static Map<String, List<Pair>> pairStorage = new HashMap<>();

    public static <I, O> void insertValue(String source, I rightValue, O leftValue) {
        Pair<I, O> pair = Pair.of(rightValue, leftValue);
        if (!pairStorage.containsKey(source)) {
            pairStorage.put(source, new LinkedList<>());
        }
        pairStorage.get(source).add(pair);
    }

    public static <I> Optional<Pair> findByRight(String source, I right) {
        List<Pair> list = pairStorage.get(source);
        if (list != null) {
            return list.stream().filter(pair -> pair.getRight().equals(right)).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static <I> Optional<Pair> findByLeft(String source, I left) {
        List<Pair> list = pairStorage.get(source);
        if (list != null) {
            return list.stream().filter(pair -> pair.getLeft().equals(left)).findFirst();
        } else {
            return Optional.empty();
        }
    }
}
