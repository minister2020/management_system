package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.Member;
import com.managementSystem.managementSystem.model.Program;
import com.managementSystem.managementSystem.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member registerMember(Member member){
        return memberRepository.save(member);
    }

    public List<Member> getMembers(){
        return memberRepository.findAll();
    }

    public List<Member> getMembersByFamily(String family) {
        List<Member> members = memberRepository.findByFamily(family);
        if (members.isEmpty()) {
            throw new NotFoundException("No family found for: " + family);
        }
        return members;
    }
}
