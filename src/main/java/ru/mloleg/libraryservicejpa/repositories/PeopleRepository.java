package ru.mloleg.libraryservicejpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mloleg.libraryservicejpa.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
