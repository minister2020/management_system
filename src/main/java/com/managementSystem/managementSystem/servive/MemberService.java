package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.constants.Constants;
import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.Member;
import com.managementSystem.managementSystem.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public ResponseEntity<?> registerMember(Member member) {
        String email = member.getEmail();
        String phone = member.getPhoneNumber();

        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Constants.PHONE_NUMBER_REQUIRED);
        }

        if (!isValidNigerianPhoneNumber(phone)) {
            return ResponseEntity.badRequest().body(Constants.INVALID_PHONE_NUMBER + " " + phone);
        }

        List<Member> existing = memberRepository.findByPhoneNumber(phone);
        if (!existing.isEmpty()) {
            return ResponseEntity.badRequest().body(Constants.PNHONE_NUMBEER_EXIST);
        }

        if (email != null && !email.isBlank()) {
            if (!isValidEmail(email)) {
                return ResponseEntity.badRequest().body(Constants.VALID_EMAIL);
            }

            List<Member> existingEmail = memberRepository.findByEmail(email);
            if (!existingEmail.isEmpty()) {
                return ResponseEntity.badRequest().body(Constants.EMAIL_EXIST);
            }
        }

        Member saved = memberRepository.save(member);
        return ResponseEntity.ok(saved);
    }


    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }
    public boolean isValidNigerianPhoneNumber(String phoneNumber) {
        String regex = "^0\\d{10}$";
        return phoneNumber != null && phoneNumber.matches(regex);
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
