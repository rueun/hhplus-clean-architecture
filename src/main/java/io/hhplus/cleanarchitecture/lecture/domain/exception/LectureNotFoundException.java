package io.hhplus.cleanarchitecture.lecture.domain.exception;

public class LectureNotFoundException extends LectureException {

    public LectureNotFoundException() {
        super(LectureErrorCode.LECTURE_NOT_FOUND);
    }

    public LectureNotFoundException(String message) {
        super(LectureErrorCode.LECTURE_NOT_FOUND, message);
    }

    public LectureNotFoundException(Throwable cause) {
        super(LectureErrorCode.LECTURE_NOT_FOUND, cause);
    }
}
