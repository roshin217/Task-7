package ru.itmentor.spring.boot_security.demo.services;

import ru.itmentor.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    Role findRoleById(Long id);
    List<Role> findAllRoles();

}
