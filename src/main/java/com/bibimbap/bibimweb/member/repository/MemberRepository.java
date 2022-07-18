package com.bibimbap.bibimweb.member.repository;

import com.bibimbap.bibimweb.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
