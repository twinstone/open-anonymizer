package application.core.hash;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashService {

    private static final String ALGORITHM = "MD5";
    private static final Logger logger = Logger.getLogger(HashService.class);

    public static String generateHash(final String input, final String secret) {
        Validate.notEmpty(input, "Input must be not empty.");
        Validate.notEmpty(secret, "Secret must be not empty.");
        try {
            logger.info(String.format("Generating new hash for input [%s] using secret [%s].", input, secret));
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(input.getBytes());
            return new String(digest.digest(secret.getBytes()), Charset.defaultCharset());
        } catch (NoSuchAlgorithmException e) {
            logger.error(String.format("Could not generate hash for input [%s].", input), e);
            return input;
        }
    }

    private HashService() {
        throw new IllegalStateException();
    }
}
