package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.*;

@Controller
@RequestMapping("/")
public class UsersControllerr {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersControllerr(UserService userService, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping(value = "user")
    public String userPageGet() {
        return "userPage";
    }

        @GetMapping(value = "/login")
    public String loginUserGet() {
        return "starterPage";
    }

    @PostMapping(value = "/login")
    public String loginUserPost(ModelMap model) {
        String login = (String) model.getAttribute("login");
        if (userService.getUserByLogin(login) == null) {
            model.addAttribute("errorText", "No user with this data exists");
            return "error";
        } else {
            return "redirect:user";
        }
    }

    @GetMapping(value = "admin")
    public String printUsers(ModelMap model) {
        List<User> userList = userService.getAllUsers();

        model.addAttribute("listUser", userList);
        return "admin/user-list";
    }

    @GetMapping(value = "/admin/new")
    public String addUserGet() {
        return "admin/user-form";
    }

    @PostMapping(value = "/admin/add")
    public String addUserPost(ModelMap model) {
        try {
            String name = (String) model.getAttribute("name");
            int age = Integer.parseInt(Objects.requireNonNull((String) model.getAttribute("age")));
            if (name.isEmpty() || age < 0 || age > 150) {
                model.addAttribute("errorText", "Incorrect user fields.");
                return "error";
            }
            String login = (String) model.getAttribute("login");
            String password = (String) model.getAttribute("password");
            String role = (String) model.getAttribute("role");

            if (userService.isExistLogin(login)) {
                model.addAttribute("errorText", "User with same login already exist.");
                return "error";
            }
            Set<Role> roles = new HashSet<>();
            if (role != null && role.equals("user")) {
                roles.add(roleService.getRoleByName("USER"));
            }
            if (role != null && role.equals("admin")) {
                roles.add(roleService.getRoleByName("ADMIN"));
                roles.add(roleService.getRoleByName("USER"));
            }
            User user = new User(login, password, role, name, age, roles);
            if (!userService.addUser(user)) {
                model.addAttribute("errorText", "Error while processing user edit.");
                return "error";
            }
            model.addAttribute("errorText", "User was added successfully!");
            return "error";
        } catch (Exception e) {
            model.addAttribute("errorText", "Error while processing user.");
            return "error";
        }
    }


    @GetMapping(value = "/admin/update")
    public String updateUserGet(@RequestParam(value = "id") String idStr, ModelMap model) {
        User user;
        try {
            int id = Integer.parseInt(idStr);
            user = userService.getUserById(id);
            if (user == null) {
                model.addAttribute("errorText", "This user doesn't exist.");
                return "error";
            }
            model.addAttribute("user", user);
            return "admin/user-form";
        } catch (Exception e) {
            model.addAttribute("errorText", "Error while processing user.");
            return "error";
        }
    }


    @PostMapping(value = "/admin/update")
    public String editUserPost(ModelMap model) {
        try {

            String name = (String) model.getAttribute("name");
            int age = Integer.parseInt(Objects.requireNonNull((String) model.getAttribute("age")));
            int id = Integer.parseInt(Objects.requireNonNull((String) model.getAttribute("id")));
            String login = (String) model.getAttribute("login");
            String password = (String) model.getAttribute("password");
            password = bCryptPasswordEncoder.encode(password);
            String role = (String) model.getAttribute("role");
            if (name.isEmpty() || age < 0 || age > 150) {
                model.addAttribute("errorText", "Incorrect user fields.");
                return "error";
            }

            Set<Role> roles = new HashSet<>();
            if (role != null && role.equals("user")) {
                roles.add(roleService.getRoleByName("USER"));
            }
            if (role != null && role.equals("admin")) {
                roles.add(roleService.getRoleByName("ADMIN"));
                roles.add(roleService.getRoleByName("USER"));
            }

            User user = new User(id, login, password, role, name, age, roles);
            model.addAttribute("user", user);
            if (!userService.updateUser(user)) {
                model.addAttribute("errorText", "Error while processing user edit.");
                return "error";
            }
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("errorText", "Error while processing user.");
            return "error";
        }
    }

    @GetMapping(value = "admin/delete")
    public String deleteUserGet(@RequestParam(value = "id") String idStr, ModelMap model) {
        User user;
        try {
            long id = Long.parseLong(idStr);
            user = userService.getUserById(id);
            if (user == null) {
                model.addAttribute("errorText", "This user doesn't exist.");
                return "error";
            }
            model.addAttribute("userForDelete", user);
            return "admin/deleteUser";
        } catch (Exception e) {
            model.addAttribute("errorText", "Error while processing user.");
            return "error";
        }
    }

    @PostMapping(value = "admin/delete")
    public String deleteUserPost(ModelMap model) {
        try {
            String name =  (String) model.getAttribute("name");
            String login =  (String) model.getAttribute("login");
            String password =  (String) model.getAttribute("password");
            String role =  (String) model.getAttribute("role");
            int age = Integer.parseInt(Objects.requireNonNull((String) model.getAttribute("age")));
            long id = Integer.parseInt(Objects.requireNonNull((String) model.getAttribute("id")));

            Set<Role> roles = new HashSet<>();
            if (role != null && role.equals("user")) {
                roles.add(roleService.getRoleByName("USER"));
            }
            if (role != null && role.equals("admin")) {
                roles.add(roleService.getRoleByName("ADMIN"));
                roles.add(roleService.getRoleByName("USER"));
            }

            User user = new User(id,login, password, role, name, age, roles);
            if (userService.deleteUser(user)) {
                return "redirect:/admin";
            }
            model.addAttribute("errorText", "Error while processing user edit.");
            return "error";

        } catch (Exception e) {
            model.addAttribute("errorText", "Error while processing user.");
            return "error";
        }
    }

    @GetMapping("/raw")
    @ResponseBody
    public String raw() {
        return "raw data";
    }
}
