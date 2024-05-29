package ru.mloleg.libraryservicejpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mloleg.libraryservicejpa.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPersonByUsername(String username);

    Optional<Person> findPersonByActivationCode(String code);

    Optional<Person> findPersonByEmail(String email);
}
