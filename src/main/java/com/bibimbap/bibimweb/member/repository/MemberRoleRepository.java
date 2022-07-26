package com.bibimbap.bibimweb.member.repository;

import com.bibimbap.bibimweb.member.domain.Member;
import com.bibimbap.bibimweb.member.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole,Long> {
    List<MemberRole> findAllByMemberId(Long id);
    List<MemberRole> findAllByRoleId(Long id);
    Optional<MemberRole> findByMemberIdAndRoleId(Long memberId, Long roleId);
    boolean existsByMemberIdAndRoleId(Long memberId, Long roleId);
    void deleteByMemberIdAndRoleId(Long memberId, Long roleId);

}
