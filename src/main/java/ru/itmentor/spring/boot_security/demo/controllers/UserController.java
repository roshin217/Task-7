package ru.itmentor.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.ServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
public class UserController {

    private final ServiceImpl service;

    @GetMapping("/admin")
    public String getAll(Model model) {
        model.addAttribute("users", service.findAll());
        return "admin";
    }

    @GetMapping("/user/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", service.findById(id));
        return "user";
    }

    @GetMapping("/admin_user/{id}")
    public String findByIdFromAdmin(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", service.findById(id));
        return "admin_user";
    }
    @GetMapping("/admin/registration")
    public String registrationFromAdmin(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", service.findAllRoles());
        return "registration";
    }
    @PostMapping("/admin")
    public String create(@ModelAttribute("user") User user, @RequestParam("roles") List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            roles.add(service.findRoleById(roleId));
        }
        user.setRoles(roles);
        service.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin_user/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", service.findById(id));
        model.addAttribute("allRoles", service.findAllRoles());
        return "edit";
    }

    @PatchMapping("/admin_user/{id}")
    public String update(@ModelAttribute("user") User user,@PathVariable("id") Long id) {
        service.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin_user/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:/admin";
    }
}
