package com.koreait.apiserver.controller;
import com.koreait.apiserver.dto.ApiResponse;
import com.koreait.apiserver.dto.LoginResponseDTO;
import com.koreait.apiserver.dto.MemberDTO;
import com.koreait.apiserver.service.JwtService;
import com.koreait.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody MemberDTO memberDTO) {
        try {
            String rawPassword = memberDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            memberService.register(memberDTO);
            return ResponseEntity.ok(ApiResponse.success());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("REGISTRATION_ERROR"));
        }
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO result = memberService.login(memberDTO.getUsername(), memberDTO.getPassword());
            if (result != null) {

                // JWT 토큰 생성
                String token = jwtService.generateToken(result.getUsername());

                // 로그인 응답 DTO 생성
                LoginResponseDTO loginResponse = new LoginResponseDTO();
                loginResponse.setToken(token);
                loginResponse.setUsername(result.getUsername());
                loginResponse.setEmail(result.getEmail());

                return ResponseEntity.ok(ApiResponse.success(loginResponse));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("LOGIN_FAILED"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("LOGIN_ERROR"));
        }
    }

    // 로그아웃
    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("AUTHORIZATION_REQUIRED"));
            }

            String token = authHeader.replace("Bearer ", "");
            String username = jwtService.getUsernameFromToken(token);
            log.info("사용자 로그아웃: {}", username);


            return ResponseEntity.ok(ApiResponse.success("LOGOUT_SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("LOGOUT_ERROR"));
        }
    }

    // 마이페이지 - 로그인한 사용자만 접근 가능
    @GetMapping("/members/me")
    public ResponseEntity<ApiResponse<MemberDTO>> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("AUTHORIZATION_REQUIRED"));
            }

            String token = authHeader.replace("Bearer ", "");
            String username = jwtService.getUsernameFromToken(token);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("INVALID_TOKEN"));
            }

            MemberDTO member = memberService.getMemberByUsername(username);
            if (member == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("USER_NOT_FOUND"));
            }

            return ResponseEntity.ok(ApiResponse.success(member));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("PROFILE_GET_ERROR"));
        }
    }

    // 마이페이지 수정 (로그인한 사용자만 가능)
    @PutMapping("/members/me")
    public ResponseEntity<ApiResponse<MemberDTO>> updateMyPage(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody MemberDTO memberDTO) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("AUTHORIZATION_REQUIRED"));
            }

            String token = authHeader.replace("Bearer ", "");
            String username = jwtService.getUsernameFromToken(token);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("INVALID_TOKEN"));
            }

            memberDTO.setUsername(username); // 본인만 수정 가능
            MemberDTO updated = memberService.updateMember(memberDTO);
            return ResponseEntity.ok(ApiResponse.success(updated));
        } catch (Exception e) {
            log.error("마이페이지 수정 실패", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("PROFILE_UPDATE_ERROR"));
        }
    }
}

// 내가 참여한 스터디룸 조회
@GetMapping("/my-studies")
public ResponseEntity<ApiResponse<List<StudyRoomDTO>>> getMyStudyRooms(
        @RequestHeader("Authorization") String authHeader) {
    try {
        log.info("참여 스터디룸 조회 요청");
        String token = authHeader.replace("Bearer ", "");
        Integer memberId = jwtService.extractMemberId(token);

        List<StudyRoomDTO> studyRooms = studyRoomService.getStudyRoomsByMemberId(memberId);
        return ResponseEntity.ok(ApiResponse.success(studyRooms));
    } catch (Exception e) {
        log.error("참여 스터디룸 조회 실패", e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("MY_STUDY_ERROR"));
    }
}

// 내가 개설한 스터디룸 조회
@GetMapping("/my-created")
public ResponseEntity<ApiResponse<List<StudyRoomDTO>>> getMyCreatedStudyRooms(
        @RequestHeader("Authorization") String authHeader) {
    try {
        log.info("개설한 스터디룸 조회 요청");
        String token = authHeader.replace("Bearer ", "");
        Integer memberId = jwtService.extractMemberId(token);

        List<StudyRoomDTO> studyRooms = studyRoomService.getStudyRoomsByBossId(memberId);
        return ResponseEntity.ok(ApiResponse.success(studyRooms));
    } catch (Exception e) {
        log.error("개설한 스터디룸 조회 실패", e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("MY_CREATED_STUDY_ERROR"));
    }
}
}