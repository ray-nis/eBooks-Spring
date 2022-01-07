package com.eBooks.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public Role create(String roleName) {
        return roleRepository.save(Role.builder().role(roleName).build());
    }

    @Transactional
    public void makeAdmin(User user) {
        Set<Role> roles = user.getRoles();

        roles.remove(roleRepository.findByRole("ROLE_USER"));
        roles.add(roleRepository.findByRole("ROLE_ADMIN"));

        user.setRoles(roles);
    }
}
