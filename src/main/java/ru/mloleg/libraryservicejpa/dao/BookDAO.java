package ru.mloleg.libraryservicejpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book;",  new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Book> show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE book_id=?;",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, publication_year) VALUES (?, ?, ?);",
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, publication_year=? WHERE book_id=?;",
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE book_id=?;", id);
    }

    public Optional<Person> getBookOwner(int id) {
        return jdbcTemplate.query("SELECT Person.* FROM Person JOIN Book ON Person.user_id = Book.user_id WHERE book_id=?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findAny();
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET user_id=NULL WHERE book_id=?", id);
    }

    public void assign(int id, Person person) {
        jdbcTemplate.update("UPDATE Book SET user_id=? WHERE book_id=?", person.getUserId(), id);
    }

}
