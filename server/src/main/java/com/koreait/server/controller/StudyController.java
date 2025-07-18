// 스터디 참가 신청
@PostMapping("/{studyRoomId}/join")
public ResponseEntity<ApiResponse<String>> joinStudyRoom(
        @PathVariable Integer studyRoomId,
        @RequestHeader("Authorization") String authHeader) {
    try {
        log.info("스터디 참가 신청 요청 - studyRoomId={}", studyRoomId);

        // 1. 로그인 여부 확인 (토큰 없으면 거부)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization 헤더 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_REQUIRED", "로그인이 필요합니다."));
        }

        // 2. JWT 토큰에서 사용자 ID 추출
        String token = authHeader.replace("Bearer ", "");
        Integer memberId = jwtService.extractMemberId(token);
        if (memberId == null) {
            log.warn("토큰에서 사용자 ID 추출 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("INVALID_TOKEN", "유효하지 않은 토큰입니다."));
        }

        // 3. 참가 신청 처리 (중복 신청/정원 초과 검증 포함)
        studyRoomService.joinStudyRoom(studyRoomId, memberId);

        // 4. 스터디 참가 신청 완료
        return ResponseEntity.ok(ApiResponse.success("스터디 신청 완료"));

    } catch (IllegalStateException e) {
        String errorMessage = e.getMessage();
        log.warn("비즈니스 예외 발생: {}", errorMessage);

        if (errorMessage.contains("이미 신청한 스터디입니다")) {
        return ResponseEntity.badRequest()
                    .body(ApiResponse.error("DUPLICATE_APPLICATION", "이미 신청한 스터디입니다."));
        } else if (errorMessage.contains("모집 정원이 초과되었습니다")) {
        return ResponseEntity.badRequest()
                    .body(ApiResponse.error("LIMIT_EXCEEDED", "모집 정원이 초과되었습니다."));
        } else {
        return ResponseEntity.badRequest()
                    .body(ApiResponse.error("JOIN_CONDITION_FAILED", errorMessage));
        }

        } catch (Exception e) {
        log.error("스터디 참가 신청 시스템 오류 - studyRoomId={}", studyRoomId, e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("SYSTEM_ERROR", "시스템 오류가 발생했습니다."));
        }
