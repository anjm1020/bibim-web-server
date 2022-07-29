package com.bibimbap.bibimweb.repository.member;

import com.bibimbap.bibimweb.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByStudentId(String studentId);
}
