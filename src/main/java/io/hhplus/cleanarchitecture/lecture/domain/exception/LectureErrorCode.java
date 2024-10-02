package io.hhplus.cleanarchitecture.lecture.domain.exception;

public enum LectureErrorCode {

    LECTURE_NOT_FOUND(404, "LECTURE_001", "해당 강의를 찾을 수 없습니다"),
    LECTURE_ITEM_NOT_FOUND(404, "LECTURE_ITEM_001", "해당 강의 아이템을 찾을 수 없습니다"),
    LECTURE_ITEM_CAPACITY_EXCEEDED(400, "LECTURE_ITEM_003", "강의 정원을 초과했습니다"),
    LECTURE_ITEM_ALREADY_ENROLLED(400, "LECTURE_ITEM_004", "이미 수강 중인 강의입니다"),
    LECTURE_ITEM_ENROLLMENT_CLOSED(400, "LECTURE_ITEM_005", "수강 신청이 마감되었습니다");

    private final int status;
    private final String code;
    private final String message;

    LectureErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
