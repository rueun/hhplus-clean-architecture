package io.hhplus.cleanarchitecture.lecture.presentation.api;


import io.hhplus.cleanarchitecture.lecture.application.dto.LectureEnrollmentInfo;
import io.hhplus.cleanarchitecture.lecture.application.dto.LectureWithItems;
import io.hhplus.cleanarchitecture.lecture.application.service.LectureService;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.presentation.dto.req.EnrollLectureRequest;
import io.hhplus.cleanarchitecture.lecture.presentation.dto.resp.LectureEnrollmentResponse;
import io.hhplus.cleanarchitecture.lecture.presentation.dto.resp.AvailableLectureForUserResponse;
import io.hhplus.cleanarchitecture.lecture.presentation.dto.resp.UserEnrollmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    /**
     * 특강 신청
     *
     * @param lectureId 특강 ID
     * @param request   특강 신청 요청
     * @return 특강 신청 결과
     */
    @PostMapping("/{lectureId}/enrollments")
    public ResponseEntity<LectureEnrollmentResponse> enroll(
            @PathVariable Long lectureId,
            @RequestBody EnrollLectureRequest request) {
        final LectureEnrollment lectureEnrollment = lectureService.enroll(request.toCommand(lectureId));
        return ResponseEntity.ok(LectureEnrollmentResponse.of(lectureEnrollment));
    }


    /**
     * 사용자가 신청 가능한 특강 목록 조회
     *
     * @param userId 사용자 ID
     * @return 사용자가 신청 가능한 특강 목록
     */
    @GetMapping("/users/{userId}/available")
    public ResponseEntity<List<AvailableLectureForUserResponse>> getAvailableLectures(
            @PathVariable Long userId) {
        final List<LectureWithItems> availableLectures = lectureService.getAvailableLectures(userId);
        return ResponseEntity.ok(availableLectures.stream()
                .map(AvailableLectureForUserResponse::of)
                .toList());
    }


    /**
     * 사용자의 특강 신청 목록 조회
     *
     * @param userId 사용자 ID
     * @return 사용자의 특강 신청 목록
     */
    @GetMapping("/users/{userId}/enrollments")
    public ResponseEntity<List<UserEnrollmentResponse>> getUserEnrollments(@PathVariable Long userId) {
        final List<LectureEnrollmentInfo> lectureEnrollmentInfos = lectureService.getUserLectureEnrollments(userId);
        return ResponseEntity.ok(
                lectureEnrollmentInfos.stream()
                        .map(UserEnrollmentResponse::of)
                        .toList()
        );
    }


    /**
     * 특강 신청 여부 조회
     *
     * @param lectureId 특강 ID
     * @param userId    사용자 ID
     * @return 특강 신청 여부
     */
    @GetMapping("/{lectureId}/enrollments/users/{userId}")
    public ResponseEntity<Boolean> checkEnrollment(
            @PathVariable Long lectureId,
            @PathVariable Long userId) {
        boolean enrolled = lectureService.checkEnrollment(lectureId, userId);
        return ResponseEntity.ok(enrolled);
    }
}
