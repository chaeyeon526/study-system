package com.koreait.apiserver.service;

import com.koreait.apiserver.dto.StudBoardDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudyBoardService {

    // 전체 스터디 목록 조회
    List<StudyRoomDTO> getStudyRoomList();

    // 스터디룸 상세보기 (roomId 기준)
    StudyRoomDTO getStudyRoomDetail(Integer studyRoomId);

    // 스터디 개설
    StudyRoomDTO createStudyRoom(StudyRoomDTO studyRoomDTO);

    //스터디룸 검색
    StudyRoomDTO getStudyRoomByTitleAndWriter(String title, String writer);


    // 스터디 참가
    void joinStudyRoom(Integer studyRoomId, Integer memberId);
    List<StudyRoomMemberDTO> getStudyRoomMembers(Integer studyRoomId);


    // front-server 연동용
    List<StudyRoomDTO> getStudyRoomsByRoomId(Integer RoomId);
}
