package application.core.hash;

import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashService {

    private static final Logger logger = Logger.getLogger(HashService.class);

    public static String generateHash(final String input, final String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            return new String(digest.digest(secret.getBytes()), Charset.defaultCharset());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Could not generate hash for inout [ " + input + " ].");
            return input;
        }
    }
}
