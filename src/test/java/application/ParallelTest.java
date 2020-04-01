package application;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParallelTest {

    private final List<Integer> l = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

    @Test
    public void test() {
        doPrint(l.stream().parallel());
    }

    private static void doPrint(Stream<Integer> s) {
        s.forEach(System.out::println);
    }
}
