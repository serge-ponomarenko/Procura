package ua.ltd.procura.procuraapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ltd.procura.procuraapp.constants.GlobalConstants;
import ua.ltd.procura.procuraapp.constants.UriStorage;
import ua.ltd.procura.procuraapp.dto.UserDto;
import ua.ltd.procura.procuraapp.entity.AppLocale;
import ua.ltd.procura.procuraapp.service.AppLocaleService;
import ua.ltd.procura.procuraapp.service.UserService;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AppLocaleService appLocaleService;

    @GetMapping(UriStorage.INDEX)
    public String home() {
        return "index";
    }

    @GetMapping(UriStorage.LOGIN)
    public String loginForm(Model model) {
        initLocales(model);
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping(UriStorage.REGISTER)
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        initLocales(model);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping(UriStorage.REGISTER_SAVE)
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {

        userService.findByEmail(user.getEmail())
                .ifPresent(u ->
                        result.rejectValue("email", "400", "There is already an account registered with that email"));
        initLocales(model);
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:" + UriStorage.REGISTER + "?success";
    }

    @GetMapping(UriStorage.USERS)
    public String listRegisteredUsers(Model model, Authentication authentication, HttpServletRequest request) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    private void initLocales(Model model) {
        if (!appLocaleService.findAll().containsKey(LocaleContextHolder.getLocale().toString()))
            LocaleContextHolder.setLocale(new Locale(GlobalConstants.DEFAULT_LOCALE_CODE));
        Map<String, AppLocale> locales = appLocaleService.findAll();
        model.addAttribute("locales", locales);
    }
}
