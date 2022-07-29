package com.bibimbap.bibimweb.service.role;

import com.bibimbap.bibimweb.repository.role.ProjectRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectRoleService {

    private final ProjectRoleRepository projectRoleRepository;

}
