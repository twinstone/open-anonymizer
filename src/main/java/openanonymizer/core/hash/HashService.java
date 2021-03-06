package openanonymizer.core.hash;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This class allows to generate hash for any {@link String} input using MD5 algorithm.
 * <p>
 * Use static methods of this class. Do not use constructor.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public final class HashService {

    private static final String ALGORITHM = "MD5";
    private static final Logger logger = Logger.getLogger(HashService.class);

    /**
     * Generates hash for char sequence input.
     *
     * @param input value
     * @param secret value
     * @return generated hash
     * */
    public static String generateHash(final String input, final String secret) {
        Validate.notEmpty(input, "Input must be not empty.");
        Validate.notEmpty(secret, "Secret must be not empty.");
        try {
            logger.info(String.format("Generating new hash for input [%s] using secret [%s].", input, secret));
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(input.getBytes(UTF_8));
            byte[] bytes = digest.digest(secret.getBytes(UTF_8));
            return new String(Base64.getMimeEncoder().encode(bytes));
        } catch (NoSuchAlgorithmException e) {
            logger.error(String.format("Could not generate hash for input [%s].", input), e);
            return input;
        }
    }

    private HashService() {
        throw new IllegalStateException(); }
}
