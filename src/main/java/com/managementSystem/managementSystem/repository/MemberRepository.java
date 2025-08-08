package com.managementSystem.managementSystem.repository;

import com.managementSystem.managementSystem.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByFamily(String family);

    boolean existsByEmail(String email);
    List<Member> findByPhoneNumber(String phoneNumber);
    List<Member> findByEmail(String email);
}
