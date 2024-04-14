package ru.mloleg.libraryservicejpa.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Book> getBooksByUserId(int id) {
        if (peopleRepository.findById(id).isPresent()) {
            Hibernate.initialize(peopleRepository.findById(id).get().getBooks());

            for (Book b: peopleRepository.findById(id).get().getBooks()) {
                if (Math.abs(b.getTakenAt().getTime() - new Date().getTime()) > 604_800_000) {
                    b.setExpired(true);
                }
            }

            return peopleRepository.findById(id).get().getBooks();
        } else {
            return null;
        }
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setUserId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
