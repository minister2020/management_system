package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.model.Member;
import com.managementSystem.managementSystem.model.Program;
import com.managementSystem.managementSystem.servive.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public Member registerMember(@RequestBody Member member) {
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

}
