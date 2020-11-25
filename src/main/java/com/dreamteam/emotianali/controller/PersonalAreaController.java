package com.dreamteam.emotianali.controller;

import com.dreamteam.emotianali.entity.User;
import com.dreamteam.emotianali.service.UserService;
import com.google.gson.Gson;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PersonalAreaController {

    private final UserService userService;

    @GetMapping("/personal_area")
    public String returnPersonalPage(Model model) {
        User currentUser = userService.getUserFromContext();
        model.addAttribute("currentUser", currentUser);
        return "personal_area";
    }

    @PostMapping("/personal_area")
    public String updateUserFromForm(@ModelAttribute("userForm") User userFromForm, Model model) {
        /*
           что нужно делать по приходе формы
           1. проверить, правильно ли пользователь пароль для подтвержения изменений. если нет, то вернуть с ошибкой
                если правильно, то можно начинать перезапись пользователя
           2. перезапись пользователя
                1. проверить, надо ли менять пароль. если да, то провести с ним операцию проверки и перезаписи
                    если нет, то не менять и перезаписать остальные записи
           3. если всё ок, то вернуться на стартовую страницу
         */
        User currentUser = userService.getUserFromContext();
        model.addAttribute("currentUser", currentUser);

        if (!userService.isCurrentPasswordSameAs(userFromForm.getPassword())) {
            model.addAttribute("error", "Wrong password");
            return "personal_area";
        } else {
            userFromForm.setId(currentUser.getId());
            userFromForm.setUsername(currentUser.getUsername()); // смешали currentUser и данные из формы
            boolean passwordWasChanged;
            if (userFromForm.getPasswordNew().isEmpty() && userFromForm.getPasswordNewConfirm().isEmpty()) {
                passwordWasChanged = false;
            } else {
                if (!userFromForm.getPasswordNew().equals(userFromForm.getPasswordNewConfirm())) {
                    model.addAttribute("error", "Passwords do not match");
                    return "personal_area";
                } else {
                    passwordWasChanged = true;
                }
            }
            if (!userService.updateUser(userFromForm, passwordWasChanged)) {
                model.addAttribute("error", "Something went wrong");
                return "personal_area";
            }
        }
        return "redirect:/logout";
    }

    @GetMapping("/personal_area/delete_account")
    public String deleteAccount(Model model) {
        User currentUser = userService.getUserFromContext();
        userService.changeRole(currentUser.getId(), "blocked");
        return "redirect:/logout";
    }
}
