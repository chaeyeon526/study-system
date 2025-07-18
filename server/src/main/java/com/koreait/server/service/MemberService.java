package com.koreait.apiserver.service;

import com.koreait.apiserver.dto.MemberDTO;

import java.util.List;
import java.util.Map;

public interface MemberService {
    void register(MemberDTO member);
    MemberDTO login(String username, String password);
    void logout(String token);
    MemberDTO getMemberByUsername(String username);
    MemberDTO getMemberById(Long id); // ID 기반 조회 추가
    MemberDTO updateMember(MemberDTO member);
}
