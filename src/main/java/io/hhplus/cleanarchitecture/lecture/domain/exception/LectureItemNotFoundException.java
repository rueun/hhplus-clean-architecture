package io.hhplus.cleanarchitecture.lecture.domain.exception;

public class LectureItemNotFoundException extends LectureException {

    public LectureItemNotFoundException() {
        super(LectureErrorCode.LECTURE_ITEM_NOT_FOUND);
    }

    public LectureItemNotFoundException(String message) {
        super(LectureErrorCode.LECTURE_ITEM_NOT_FOUND, message);
    }

    public LectureItemNotFoundException(Throwable cause) {
        super(LectureErrorCode.LECTURE_ITEM_NOT_FOUND, cause);
    }
}
