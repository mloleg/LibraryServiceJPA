package ru.mloleg.libraryservicejpa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Person")
@Data
@Getter
@Setter
public class Person implements Serializable {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "username")
    private String username;

    @Email
    @Column(name = "email")
    private String email;

    @Size(min = 2, message = "Password must be at least 2 characters long")
    @Column(name = "password")
    private String password;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotEmpty(message = "Person credentials should not be empty")
    @Size(min = 2, max = 100, message = "Person credentials should be between 2 and 100 characters long")
    @Column(name = "credentials")
    private String credentials;

    @Min(value = 1900, message = "Person birth date should not be earlier than 1900")
    @Column(name = "birth_date")
    private int birthDate;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Book> books;

    @Column(name = "role")
    private String role;

    public Person() {}

    public Person(int userId, String credentials, int birthDate) {
        this.userId = userId;
        this.credentials = credentials;
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
