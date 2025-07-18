package com.koreait.apiserver.dao;

import com.koreait.apiserver.entity.StudyRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

@Mapper
public interface StudyRoomDao {
    
    // 스터디 개설
    int insertStudyRoom(StudyRoom studyRoom);

    // 스터디룸 검색(제목+작성자).java
    Optional<StudyRoom> findByTitleAndWriter(String title, String writer);

    // 모든 스터디룸 조회 (페이징)
    List<StudyRoom> findAll(@Param("limit") int limit, @Param("offset") int offset);

    //스터디룸 상세보기
    Optional<StudyRoom> findByRoomId(@Param("roomId") Integer roomId);

    
}