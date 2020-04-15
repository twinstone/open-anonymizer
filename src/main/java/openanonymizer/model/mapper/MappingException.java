package openanonymizer.model.mapper;

/**
 * Mapping exception that could be thrown by {@link EntityWrapperMapper} if mapping failed.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class MappingException extends RuntimeException {
    public MappingException(Throwable cause) {
        super(cause);
    }
}
