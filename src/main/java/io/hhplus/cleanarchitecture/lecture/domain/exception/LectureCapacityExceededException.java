package io.hhplus.cleanarchitecture.lecture.domain.exception;

public class LectureCapacityExceededException extends LectureException {

    public LectureCapacityExceededException() {
        super(LectureErrorCode.LECTURE_ITEM_CAPACITY_EXCEEDED);
    }

    public LectureCapacityExceededException(String message) {
        super(LectureErrorCode.LECTURE_ITEM_CAPACITY_EXCEEDED, message);
    }

    public LectureCapacityExceededException(Throwable cause) {
        super(LectureErrorCode.LECTURE_ITEM_CAPACITY_EXCEEDED, cause);
    }
}
