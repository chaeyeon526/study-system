package com.koreait.apiserver.entity;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Integer id;
    private String userid;
    private String email;
    private String phone;
    private String nickname;
    private String name;
    private String password;
    private String gender;
    private String region;  // addr1, addr2를 region으로 통합
    private String district;  // 구/군
    private String profileImage;
    private String status = "ACTIVE";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getter/Setter 메서드들
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 기존 필드들과의 호환성을 위한 getter/setter
    public String getUsername() {
        return this.userid;
    }

    public void setUsername(String username) {
        this.userid = username;
    }

    public String getEmail() {
        return this.userid;
    }

    public void setEmail(String email) {
        this.userid = email;
    }

    public LocalDateTime getRegDate() {
        return this.createdAt;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.createdAt = regDate;
    }
}
