package io.hhplus.cleanarchitecture.lecture.domain.exception;

import io.hhplus.cleanarchitecture.common.exception.BusinessException;

public class LectureException extends BusinessException {
    public LectureException(LectureErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }

    public LectureException(LectureErrorCode errorCode, String message) {
        super(errorCode.getCode(), message);
    }

    public LectureException(LectureErrorCode errorCode, Throwable cause) {
        super(errorCode.getCode(), errorCode.getMessage());
        initCause(cause);
    }
}
