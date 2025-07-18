package com.koreait.apiserver.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDTO {
    private Integer id;
    private String userid;
    private String email;
    private String phone;
    private String nickname;
    private String name;
    private String password;
    private String gender;
    private String region;  //
    private String district;  // 구/군
    private String profileImage;
    private String status = "ACTIVE";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기존 필드들과의 호환성을 위한 getter/setter
    public String getUsername() {
        return this.userid;
    }

    public void setUsername(String username) {
        this.userid = username;
    }

    public String getEmail() {return this.userid;}

    public LocalDateTime getRegDate() {
        return this.createdAt;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.createdAt = regDate;
    }

}