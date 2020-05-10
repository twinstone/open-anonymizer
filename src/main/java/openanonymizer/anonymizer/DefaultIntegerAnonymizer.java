package openanonymizer.anonymizer;

public class DefaultIntegerAnonymizer extends DefaultAnonymizer<Integer> {
    @Override
    Integer getDefaultValue(String input, String[] params) {
        return Integer.parseInt(input);
    }
}
