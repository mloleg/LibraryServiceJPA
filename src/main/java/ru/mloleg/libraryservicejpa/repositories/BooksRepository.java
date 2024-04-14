package ru.mloleg.libraryservicejpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mloleg.libraryservicejpa.models.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByTitleIgnoreCaseStartingWith(String title);
}
