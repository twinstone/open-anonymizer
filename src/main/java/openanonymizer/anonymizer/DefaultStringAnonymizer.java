package openanonymizer.anonymizer;

public class DefaultStringAnonymizer extends DefaultAnonymizer<String> {
    @Override
    String getDefaultValue(String input, String[] params) {
        return input;
    }
}
