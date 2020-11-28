package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.entity.Role;
import com.dreamteam.emotianali.entity.User;
import com.dreamteam.emotianali.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final MailService mailService;

    private final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder; // с этим надо что-то сделать, наверное, но я хз

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userFromDB = userRepository.findByUsername(username);
        if (userFromDB == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userFromDB;
    }

    public User getUserFromContext() {
        Object currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUserDetails.equals("anonymousUser")) {
            return null;
        } else {
            return (User) currentUserDetails;
        }
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }
        int id = (user.getUsername() + user.getEmail()).hashCode();
        user.setId((long) id);
        user.setRoles(Collections.singleton(new Role(3L, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean updateUser(User userFromForm, boolean passwordWasChanged) {
        User userFromDB = userRepository.findById(userFromForm.getId()).orElseThrow(RuntimeException::new);

        if (!deleteUser(userFromForm.getId())) {
            return false;
        }

        userFromDB.setImageLink(userFromForm.getImageLink());
        userFromDB.setFirstName(userFromForm.getFirstName());
        userFromDB.setLastName(userFromForm.getLastName());
        userFromDB.setBirthday(userFromForm.getBirthday());
        userFromDB.setUserGroup(userFromForm.getUserGroup());
        userFromDB.setEmail(userFromForm.getEmail());
        userFromDB.setRecords(userFromForm.getRecords());

        if (passwordWasChanged) {
            return updateWithPassword(userFromDB, userFromForm.getPasswordNew());
        } else {
            return updateWithoutPassword(userFromDB);
        }
    }

    private boolean updateWithPassword(User userUpdated, String passwordNew) {
        userUpdated.setPassword(passwordEncoder.encode(passwordNew));
        userRepository.save(userUpdated);
        return true;
    }

    private boolean updateWithoutPassword(User userUpdated) {
        userRepository.save(userUpdated);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public boolean changeRole(Long userId, String role) {
        User userFromDB = userRepository.findById(userId).orElseThrow(RuntimeException::new);

        if (!deleteUser(userId)) {
            return false;
        }
        if (role.equals("analyst")) {
            userFromDB.setRoles(Collections.singleton(new Role(2L, "ROLE_ANALYST")));
        } else if (role.equals("user")) {
            userFromDB.setRoles(Collections.singleton(new Role(3L, "ROLE_USER")));
        } else if (role.equals("blocked")) {
            userFromDB.setRoles(Collections.singleton(new Role(0L, "ROLE_BLOCKED")));
        }
        mailService.send(userFromDB.getEmail(), false, role);
        userRepository.save(userFromDB);
        return true;
    }

    public boolean isCurrentPasswordSameAs(String passwordAnother) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserPassword = currentUser.getPassword();
        return passwordEncoder.matches(passwordAnother, currentUserPassword);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
