package io.hhplus.cleanarchitecture.lecture.domain.exception;

public class LectureEnrollmentClosedException extends LectureException {

    public LectureEnrollmentClosedException() {
        super(LectureErrorCode.LECTURE_ITEM_ENROLLMENT_CLOSED);
    }

    public LectureEnrollmentClosedException(String message) {
        super(LectureErrorCode.LECTURE_ITEM_ENROLLMENT_CLOSED, message);
    }

    public LectureEnrollmentClosedException(Throwable cause) {
        super(LectureErrorCode.LECTURE_ITEM_ENROLLMENT_CLOSED, cause);
    }
}
