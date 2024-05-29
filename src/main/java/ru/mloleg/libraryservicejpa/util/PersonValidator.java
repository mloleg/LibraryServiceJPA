package ru.mloleg.libraryservicejpa.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mloleg.libraryservicejpa.dto.PersonDTO;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.repositories.PeopleRepository;

@Component
public class PersonValidator implements Validator {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;

        if (peopleRepository.findPersonByUsername(personDTO.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "This username is already in use");
        }

        if (peopleRepository.findPersonByEmail(personDTO.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already in use");
        }

        if (!personDTO.getPassword().equals(personDTO.getPasswordConfirmation())) {
            errors.rejectValue("password", "", "Passwords should be identical");
            errors.rejectValue("passwordConfirmation", "", "Passwords should be identical");
        }
    }
}
