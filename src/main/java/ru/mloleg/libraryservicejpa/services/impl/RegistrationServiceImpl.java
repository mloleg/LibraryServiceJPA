package ru.mloleg.libraryservicejpa.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.repositories.PeopleRepository;
import ru.mloleg.libraryservicejpa.services.MailService;
import ru.mloleg.libraryservicejpa.services.RegistrationService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public RegistrationServiceImpl(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setActivationCode(UUID.randomUUID().toString());
        person.setIsActive(false);

        log.info("Sending email to:" + person.getCredentials());
        mailService.sendConfirmationCode(person);
        log.info("Message sent");
        peopleRepository.save(person);
    }

    @Transactional
    public boolean activatePerson(String code) {
        Optional<Person> person = peopleRepository.findPersonByActivationCode(code);

        if (person.isPresent()) {
            person.get().setActivationCode(null);
            peopleRepository.save(person.get());
            person.get().setIsActive(true);

            return true;
        } else {
            return false;
        }
    }
}
