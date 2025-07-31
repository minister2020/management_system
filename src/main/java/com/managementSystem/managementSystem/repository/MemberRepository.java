package com.managementSystem.managementSystem.repository;

import com.managementSystem.managementSystem.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByFamily(String family);
}
