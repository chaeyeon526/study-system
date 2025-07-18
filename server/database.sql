-- =============================================
-- TheBridgeHub 데이터베이스 스키마 (완전판)
-- 채팅 로그 시스템 + 스터디 참가 시스템 포함
-- =============================================

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS thebridgehub 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 데이터베이스 사용
USE thebridgehub;

-- =============================================
-- 1. 회원 테이블
-- =============================================
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 ID (PK)',
    userid VARCHAR(100) NOT NULL UNIQUE COMMENT '아이디(이메일)',
    phone VARCHAR(20) COMMENT '전화번호',
    nickname VARCHAR(50) COMMENT '닉네임',
    name VARCHAR(50) NOT NULL COMMENT '이름',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호 (암호화)',
    gender ENUM('남자','여자') COMMENT '성별',
    region VARCHAR(100) COMMENT '지역',
    district VARCHAR(100) COMMENT '구/군',
    profile_image VARCHAR(500) COMMENT '프로필 이미지 경로',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    INDEX idx_members_description (description(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원 정보';

-- =============================================
-- 2. 스터디 게시판 테이블 (Java 엔티티명: studyboard)
-- =============================================
CREATE TABLE studyroom (
    study_room_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '스터디룸 ID',
    room_id INT NOT NULL UNIQUE COMMENT '채팅방 ID (FK)',
    boss_id INT NOT NULL COMMENT '방장 ID',
    title VARCHAR(255) NOT NULL COMMENT '스터디 제목',
    description TEXT COMMENT '스터디 설명',
    capacity INT DEFAULT 10 COMMENT '최대 참가자 수',
    current_members INT DEFAULT 1 COMMENT '현재 참가자 수',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    deadline DATETIME NOT NULL COMMENT '스터디 모집 마감일',
    
    FOREIGN KEY (room_id) REFERENCES ChatRoom(room_id) ON DELETE CASCADE,
    FOREIGN KEY (boss_id) REFERENCES members(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='스터디게시판';
