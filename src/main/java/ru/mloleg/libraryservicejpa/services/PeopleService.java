package ru.mloleg.libraryservicejpa.services;

import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.models.Person;

import java.util.Arrays;
import java.util.List;

public interface PeopleService {
    List<Person> getAll();

    Person getById(int id);

    Person getByUsername(String username);

    List<Book> getBooksByUserId(int id);

    Person create(Person person);

    Person update(int id, Person person);

    void delete(int id);
}
