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
public class PeopleDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PeopleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person;",  new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> show(int userId) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE user_id=?;",
                        new Object[]{userId},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(credentials, birth_date) VALUES (?, ?);",
                person.getCredentials(),
                person.getBirthDate());
    }

    public void update(int userId, Person person) {
        System.out.println(jdbcTemplate.update("UPDATE Person SET credentials=?, birth_date=? WHERE user_id=?;",
                person.getCredentials(),
                person.getBirthDate(),
                userId));
    }

    public void delete(int userId) {
        jdbcTemplate.update("DELETE FROM Person WHERE user_id=?;", userId);
    }

    public List<Book> getBooksByUserId(int userId) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE user_id=?",
                new Object[]{userId},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
