package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.Children;
import com.managementSystem.managementSystem.model.Member;
import com.managementSystem.managementSystem.model.UpdateChildRequest;
import com.managementSystem.managementSystem.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public ResponseEntity<?> registerMember(Member member) {

        Member saved = memberRepository.save(member);
        return ResponseEntity.ok(saved);
    }

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    public List<Member> getMembersByFamily(String family) {
        List<Member> members = memberRepository.findByFamily(family);
        if (members.isEmpty()) {
            throw new NotFoundException("No family found for: " + family);
        }
        return members;
    }

    public Member updateWorkInfo(Long id, String workInfo) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        member.setWorkInfo(workInfo);
        return memberRepository.save(member);
    }

    public Member updateAddress(Long id, String address) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        member.setAddress(address);
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateChild(Long memberId, UpdateChildRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Children child = member.getChildren().stream()
                .filter(c -> c.getChildName().equalsIgnoreCase(request.getChildName())) // <-- use "name" not "childName"
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Child not found"));

        child.setSchool(request.getSchool());
        child.setAge(request.getAge());
        child.setSchoolClass(request.getSchoolClass());

        return memberRepository.save(member);

    }
}
