package ru.mloleg.libraryservicejpa.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.libraryservicejpa.exception.ResourceNotFoundException;
import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.repositories.BooksRepository;
import ru.mloleg.libraryservicejpa.services.BooksService;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getAll() {
        return booksRepository.findAll();
    }

    @Cacheable(value = "BooksService::getById", key = "#id")
    public Book getById(int id) {
        return booksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));
    }

    public List<Book> getWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("publicationYear")))
                    .getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public List<Book> getByTitle(String title) {
        return booksRepository.findBookByTitleIgnoreCaseStartingWith(title);
    }

    @Transactional
    @Cacheable(value = "BooksService::getById", key = "#book.bookId")
    public Book create(Book book) {
        return booksRepository.save(book);
    }

    @Transactional
    @CachePut(value = "BooksService::getById", key = "#book.bookId")
    public Book update(int id, Book book) {
        book.setBookId(id);
        book.setOwner(booksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found")).getOwner());

        return booksRepository.save(book);
    }

    @Transactional
    @CacheEvict(value = "BooksService::getById", key = "#id")
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    @CachePut(value = "BooksService::getById", key = "#id")
    public Book release(int id) {
        Book releasedBook = booksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));
        releasedBook.setOwner(null);
        releasedBook.setTakenAt(null);

        return releasedBook;
    }

    @Transactional
    @CachePut(value = "BooksService::getById", key = "#id")
    public Book assign(int id, Person person) {
        Book assignedBook = booksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));
        assignedBook.setOwner(person);
        assignedBook.setTakenAt(new Date());

        return assignedBook;
    }
}
