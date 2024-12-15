package moja.refrigerator.exception.user;

import moja.refrigerator.exception.common.BusinessException;

public class DuplicateUserException extends BusinessException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
