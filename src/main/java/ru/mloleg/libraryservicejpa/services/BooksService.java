package ru.mloleg.libraryservicejpa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.repositories.BooksRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("publicationYear")))
                    .getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Person getBookOwner(int id) {
        if (booksRepository.findById(id).isPresent()) {
            return booksRepository.findById(id).get().getOwner();
        } else {
            return null;
        }
    }

    public List<Book> findBookByTitle(String query) {
        return booksRepository.findBookByTitleIgnoreCaseStartingWith(query);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setBookId(id);
        book.setOwner(booksRepository.findById(id).get().getOwner());
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        if (booksRepository.findById(id).isPresent()) {
            booksRepository.findById(id).get().setOwner(null);
            booksRepository.findById(id).get().setTakenAt(null);
        }
    }

    @Transactional
    public void assign(int id, Person person) {
        if (booksRepository.findById(id).isPresent()) {
            booksRepository.findById(id).get().setOwner(person);
            booksRepository.findById(id).get().setTakenAt(new Date());
        }
    }
}
