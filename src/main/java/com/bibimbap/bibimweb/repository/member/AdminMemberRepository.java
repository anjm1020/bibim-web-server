package com.bibimbap.bibimweb.repository.member;

import com.bibimbap.bibimweb.domain.member.AdminMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<AdminMember, Long> {
    
}
