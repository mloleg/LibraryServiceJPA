package ru.mloleg.libraryservicejpa.services.impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.libraryservicejpa.exception.ResourceNotFoundException;
import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.repositories.PeopleRepository;
import ru.mloleg.libraryservicejpa.services.PeopleService;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleServiceImpl implements PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleServiceImpl(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getAll() {
        return peopleRepository.findAll();
    }

    @Cacheable(value = "PeopleService::getById", key = "#id")
    public Person getById(int id) {
        return peopleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Cacheable(value = "PeopleService::getByUsername", key = "#username")
    public Person getByUsername(String username) {
        return peopleRepository.findPersonByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public List<Book> getBooksByUserId(int id) {
        Person person = peopleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Hibernate.initialize(person.getBooks());

        for (Book b : person.getBooks()) {
            if (Math.abs(b.getTakenAt().getTime() - new Date().getTime()) > 604_800_000) {
                b.setExpired(true);
            }
        }

        return person.getBooks();
    }

    @Transactional
    @Caching(cacheable = {
            @Cacheable(value = "PeopleService::getById", key = "#person.userId"),
            @Cacheable(value = "PeopleService::getByUsername", key = "#person.username")
    })
    public Person create(Person person) {
        return peopleRepository.save(person);
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "PeopleService::getById", key = "#person.userId"),
            @CachePut(value = "PeopleService::getByUsername", key = "#person.username")
    })
    public Person update(int id, Person person) {
        person.setUserId(id);
        return peopleRepository.save(person);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "PeopleService::getById", key = "#id"),
            @CacheEvict(value = "PeopleService::getByUsername", key = "#peopleRepository.findById(id).get().username")
    })
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
