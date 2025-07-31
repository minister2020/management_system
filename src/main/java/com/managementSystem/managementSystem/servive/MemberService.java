package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.Member;
import com.managementSystem.managementSystem.model.Program;
import com.managementSystem.managementSystem.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Member updateMember(String family, Member updatedMember) {
        Optional<Member> optionalMember = memberRepository.findById(Long.valueOf(family));
        if (optionalMember.isEmpty()) {
            throw new NotFoundException("Member not found with id: " + family);
        }

        Member member = optionalMember.get();

        // Update basic fields
        member.setName(updatedMember.getName());
        member.setGender(updatedMember.getGender());
        member.setWorkInfo(updatedMember.getWorkInfo());
        member.setFamily(updatedMember.getFamily());

        // Update email only if it's different and unique
        if (!member.getEmail().equals(updatedMember.getEmail())) {
            if (memberRepository.existsByEmail(updatedMember.getEmail())) {
                throw new RuntimeException("Email already exists: " + updatedMember.getEmail());
            }
            member.setEmail(updatedMember.getEmail());
        }

        // Update spouse and kids
        member.setSpouse(updatedMember.getSpouse());
        member.setKids(updatedMember.getKids());

        return memberRepository.save(member);
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
}
