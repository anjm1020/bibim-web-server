package com.bibimbap.bibimweb.member.service;

import com.bibimbap.bibimweb.member.domain.Role;
import com.bibimbap.bibimweb.member.dto.role.RoleDto;
import com.bibimbap.bibimweb.member.dto.role.RoleResponseDto;
import com.bibimbap.bibimweb.member.dto.role.RoleUpdateDto;
import com.bibimbap.bibimweb.member.repository.MemberRoleRepository;
import com.bibimbap.bibimweb.member.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;

    private final ModelMapper mapper = new ModelMapper();

    public boolean isExistRole(String groupName, String role) {
        return roleRepository.existsByGroupNameAndRole(groupName, role);
    }

    public RoleDto addRole(RoleUpdateDto dto) {
        Role saved = roleRepository.save(mapper.map(dto, Role.class));
        return mapper.map(saved, RoleDto.class);
    }

    public Long getRoleIdByGroupIdAndGroupType(Long id, String type, String role) {
        return roleRepository.findByGroupIdAndGroupTypeAndRole(id, type, role).get().getId();
    }

    public Long getLeaderRoleOfGroup(Long groupId, String groupType, String role) {
        return roleRepository.findByGroupIdAndGroupTypeAndRole(groupId, groupType, role).get().getId();
    }

    public Long getMemberRoleOfGroup(Long memberId, Long groupId, String groupType, String role) {
        List<RoleDto> roles = getRoleOfMember(memberId);
        return roles.stream()
                .filter(o -> o.getGroupId() == groupId && o.getGroupType() == groupType && o.getRole() == role)
                .findFirst()
                .get().getId();
    }
    public List<RoleDto> getRoleOfMember(Long memberId) {
        return memberRoleRepository.findAllByMemberId(memberId)
                .stream()
                .map(o->mapper.map(o.getRole(), RoleDto.class))
                .collect(Collectors.toList());
    }
}
