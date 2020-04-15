package openanonymizer.datasource;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class allows to generate connection url for database.
 * Class is not responsible for url validation and connection testing.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class ConnectionUriBuilder {

    private static final String HOST_PROPERTY = "host";
    private static final String PORT_PROPERTY = "port";
    private static final String USER_PROPERTY = "user";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String DB_PROPERTY = "database";
    private static final String PARAMS_PROPERTY = "params";

    /**
     * Generates connection from parameters in json file.
     *
     * @param prefix       of url
     * @param node         json node with properties
     * @param authenticate append authentication credentials
     * @return connection url as char sequence
     */
    public static String uriFromJson(String prefix, JsonNode node, boolean authenticate) {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(prefix);
        uriBuilder.append("://");
        if (authenticate) {
            if (node.hasNonNull(USER_PROPERTY)) {
                uriBuilder.append(node.get(USER_PROPERTY).textValue());
                uriBuilder.append(":");
            }
            if (node.hasNonNull(PASSWORD_PROPERTY)) {
                uriBuilder.append(node.get(PASSWORD_PROPERTY).textValue());
                uriBuilder.append("@");
            }
        }
        if (node.hasNonNull(HOST_PROPERTY)) {
            uriBuilder.append(node.get(HOST_PROPERTY).textValue());
            uriBuilder.append(":");
        }
        if (node.hasNonNull(PORT_PROPERTY)) {
            uriBuilder.append(node.get(PORT_PROPERTY).intValue());
            uriBuilder.append("/");
        }
        if (node.hasNonNull(DB_PROPERTY)) {
            uriBuilder.append(node.get(DB_PROPERTY).textValue());
        }
        if (node.hasNonNull(PARAMS_PROPERTY)) {
            uriBuilder.append("?");
            uriBuilder.append(node.get(PARAMS_PROPERTY).textValue());
        }
        return uriBuilder.toString();
    }

    private ConnectionUriBuilder() {
        throw new IllegalStateException();
    }
}
