package com.koreait.apiserver.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudyRoomDTO {
    private Integer studyRoomId;
    private Integer roomId;
    private String title;
    private String description;
    private Integer capacity = 10;
    private Integer currentMembers = 1;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    
    // 현재 참가 중인 모든 멤버의 닉네임 목록
    private List<String> memberNicknames;
} 