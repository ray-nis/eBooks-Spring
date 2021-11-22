package com.eBooks.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public Role create(String roleName) {
        return roleRepository.save(Role.builder().role(roleName).build());
    }
}
