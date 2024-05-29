package ru.mloleg.libraryservicejpa.services;


import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.models.Person;

import java.util.List;

public interface BooksService {

    List<Book> getAll();

    Book getById(int id);

    List<Book> getWithPagination(Integer page, Integer booksPerPage, boolean sortBy);

    List<Book> getByTitle(String title);

    Book create(Book book);

    Book update(int id, Book book);

    void delete(int id);

    Book release(int id);

    Book assign(int id, Person person);
}
