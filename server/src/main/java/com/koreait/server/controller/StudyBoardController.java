package com.koreait.apiserver.controller;

import com.koreait.apiserver.dto.ApiResponse;
import com.koreait.apiserver.dto.StudyBoardDTO;
import com.koreait.apiserver.service.StudyRoomService;
import com.koreait.apiserver.service.JwtService;
import com.koreait.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyBoardController {
    @Autowired
    private final StudyBoardService studyboardService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final MemberService memberService;


    // 스터디룸 전체 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudyRoomDTO>>> getStudyRoomList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            log.info("스터디룸 목록 조회 요청 - page: {}, size: {}", page, size);
            Page<StudyRoomDTO> studyRoomPage = studyRoomService.getStudyRoomList(PageRequest.of(page, size));
            return ResponseEntity.ok(ApiResponse.success(studyRoomPage));
        } catch (Exception e) {
            log.error("스터디룸 목록 조회 실패", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("STUDY_LIST_ERROR"));
        }
    }
    // 스터디룸 검색 기능 (제목 + 작성자)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudyRoomDTO>>> searchStudyRooms(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String writer) {
        try {
            log.info("스터디룸 검색 요청: title={}, writer={}", title, writer);

            // 검색 서비스 호출
            List<StudyRoomDTO> resultList = studyRoomService.searchStudyRooms(title, writer);

            return ResponseEntity.ok(ApiResponse.success(resultList));
        } catch (Exception e) {
            log.error("스터디룸 검색 실패: title={}, writer={}", title, writer, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("STUDYROOM_SEARCH_FAILED", "스터디 검색 중 오류가 발생했습니다."));
        }
    }

    // 스터디 개설
    @PostMapping
    public ResponseEntity<ApiResponse<StudyRoomDTO>> createStudyRoom(@RequestBody StudyRoomDTO studyRoomDTO, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        try {
            // 요청 데이터 검증
            if (studyRoomDTO == null) {
                log.error("StudyRoomDTO가 null입니다");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("INVALID_REQUEST_DATA"));
            }

            log.info("스터디 개설 요청자 memberId: {}", memberId);
            log.info("요청 데이터: {}", studyRoomDTO);
            
            // 필수 필드 검증
            if (studyRoomDTO.getTitle() == null || studyRoomDTO.getTitle().trim().isEmpty()) {
                log.error("제목이 없습니다");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("TITLE_REQUIRED"));
            }
            if (studyRoomDTO.getDescription() == null || studyRoomDTO.getDescription().trim().isEmpty()) {
                log.error("설명이 없습니다");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("DESCRIPTION_REQUIRED"));
            }
            if (studyRoomDTO.getMaxMembers() == null || studyRoomDTO.getMaxMembers() <= 0) {
                log.error("모집 인원이 충족되지 않았습니다");
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("MAX_MEMBERS_REQUIRED"));
            }
            if (studyRoomDTO.getDeadline() == null) {
                log.error("마감일이 등록되지 않았습니다");
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("DEADLINE_REQUIRED"));
            }
            studyRoomDTO.setCreatorId(memberId);

            // JWT 토큰에서 사용자 ID 추출
            String token = authHeader.replace("Bearer ", "");
            log.info("토큰: {}", token);
            Integer memberId = jwtService.extractMemberId(token);
            
            if (memberId == null) {
                log.error("토큰에서 사용자 ID 추출 실패");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("AUTH_ERROR"));
            }

            StudyRoomDTO createdStudyRoom = studyRoomService.createStudyRoom(studyRoomDTO);
            log.info("스터디룸 생성 성공: {}", createdStudyRoom);
            return ResponseEntity.ok(ApiResponse.success(createdStudyRoom));
        } catch (Exception e) {
            log.error("스터디룸 생성 실패 - 상세 에러: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("STUDY_CREATE_ERROR"));
        }
    }