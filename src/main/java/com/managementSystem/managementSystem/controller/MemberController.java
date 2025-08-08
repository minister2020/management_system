package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.model.Member;
import com.managementSystem.managementSystem.servive.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody Member member) {
        return memberService.registerMember(member);
    }

    @GetMapping("allMembers")
    public List<Member> getAllMember() {
        return memberService.getMembers();
    }

    @GetMapping("/by-family")
    public List<Member> getMembersByFamily(@RequestParam String family) {
        return memberService.getMembersByFamily(family);
    }

    @PutMapping("/{family}")
    public Member updateMember(@PathVariable String family, @RequestBody Member updatedMember) {
        return memberService.updateMember(family, updatedMember);
    }

    @PatchMapping("/{id}/work-info")
    public Member updateWorkInfo(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String workInfo = payload.get("workInfo");
        return memberService.updateWorkInfo(id, workInfo);
    }

    @PatchMapping("/{id}/address")
    public Member updateAddress(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String address = payload.get("address");
        return memberService.updateAddress(id, address);
    }
}
