package ru.mloleg.libraryservicejpa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotEmpty(message = "Person credentials should not be empty")
    @Size(min = 2, max = 100, message = "Person credentials should be between 2 and 100 characters long")
    @Column(name = "credentials")
    private String credentials;

    @Min(value = 1900, message = "Person birth date should not be earlier than 1900")
    @Column(name = "birth_date")
    private int birthDate;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {}

    public Person(int userId, String credentials, int birthDate) {
        this.userId = userId;
        this.credentials = credentials;
        this.birthDate = birthDate;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Person{" +
                "userId=" + userId +
                ", credentials='" + credentials + '\'' +
                ", birthDate=" + birthDate +
                ", books=" + books +
                '}';
    }
}
