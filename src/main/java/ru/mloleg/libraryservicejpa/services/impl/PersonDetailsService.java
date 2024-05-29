package ru.mloleg.libraryservicejpa.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.repositories.PeopleRepository;
import ru.mloleg.libraryservicejpa.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findPersonByUsername(username);

        if (person.isPresent()) {
            return new PersonDetails(person.get());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
