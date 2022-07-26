package com.bibimbap.bibimweb.member.repository;

import com.bibimbap.bibimweb.member.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByGroupNameAndRole(String group, String role);
    Optional<Role> findByGroupNameAndRole(String group, String role);
    boolean existsByGroupNameAndRole(String group, String role);
}
