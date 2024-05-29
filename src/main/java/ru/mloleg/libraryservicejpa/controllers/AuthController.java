package ru.mloleg.libraryservicejpa.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mloleg.libraryservicejpa.dto.PersonDTO;
import ru.mloleg.libraryservicejpa.exception.UserActivationException;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.services.RegistrationService;
import ru.mloleg.libraryservicejpa.util.PersonValidator;
import ru.mloleg.libraryservicejpa.util.mappers.PersonMapper;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final PersonMapper personMapper;
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(PersonMapper personMapper,
                          PersonValidator personValidator,
                          RegistrationService registrationService) {
        this.personMapper = personMapper;
        this.personValidator = personValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("url", "http://localhost:8080/auth/login");
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") PersonDTO person) {
        return "auth/registration";
    }

    @GetMapping("/activation/{code}")
    public String activate(@PathVariable("code") String code) {
        if (registrationService.activatePerson(code)) {
            log.info("User successfully activated!");
        } else {
            throw new UserActivationException("User was not activated...");
        }

        return "auth/login";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid PersonDTO personDTO,
                                      BindingResult bindingResult) {
        Person person = personMapper.toEntity(personDTO);

        personValidator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(person);

        return "redirect:/auth/login";
    }
}
