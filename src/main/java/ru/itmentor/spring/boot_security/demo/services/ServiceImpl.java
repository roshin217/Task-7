package ru.itmentor.spring.boot_security.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ServiceImpl implements UserDetailsService, UserService, RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException(username + " not found!");

        return user.get();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new UsernameNotFoundException("user not found!");

        return user.get();
    }

    @Transactional
    @Override
    public void save(User user) {
        user.setRoles(getNewRoles(user));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void update(Long id, User user) {

        User userToBeUpdated = findById(id);
        userToBeUpdated.setUsername(user.getUsername());
        userToBeUpdated.setPassword(user.getPassword());
        user.setRoles(getNewRoles(user));
        userRepository.save(userToBeUpdated);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Role findRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.get();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    private Set<Role> getNewRoles(User user) {

        Set<Role> newRoles = user.getRoles().stream()
                .map(role -> roleRepository.findByRoleName(role.getRoleName()))
                .collect(Collectors.toSet());
        return newRoles;
    }
}
