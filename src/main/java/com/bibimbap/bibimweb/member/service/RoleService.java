package com.bibimbap.bibimweb.member.service;

import com.bibimbap.bibimweb.member.domain.Role;
import com.bibimbap.bibimweb.member.dto.role.RoleResponseDto;
import com.bibimbap.bibimweb.member.dto.role.RoleUpdateDto;
import com.bibimbap.bibimweb.member.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final ModelMapper mapper = new ModelMapper();

    public boolean isExistRole(String groupName, String role) {
        return roleRepository.existsByGroupNameAndRole(groupName, role);
    }

    public RoleResponseDto addRole(RoleUpdateDto dto) {
        Role saved = roleRepository.save(mapper.map(dto, Role.class));
        return mapper.map(saved, RoleResponseDto.class);
    }

    // 특정 멤버의 role 조회
    // role update
}
