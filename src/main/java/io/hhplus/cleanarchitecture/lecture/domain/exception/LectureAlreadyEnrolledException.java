package io.hhplus.cleanarchitecture.lecture.domain.exception;

public class LectureAlreadyEnrolledException extends LectureException {

    public LectureAlreadyEnrolledException() {
        super(LectureErrorCode.LECTURE_ITEM_ALREADY_ENROLLED);
    }

    public LectureAlreadyEnrolledException(String message) {
        super(LectureErrorCode.LECTURE_ITEM_ALREADY_ENROLLED, message);
    }

    public LectureAlreadyEnrolledException(Throwable cause) {
        super(LectureErrorCode.LECTURE_ITEM_ALREADY_ENROLLED, cause);
    }
}
