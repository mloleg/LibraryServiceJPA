package ru.mloleg.libraryservicejpa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.mloleg.libraryservicejpa.models.Book;

import java.util.List;

@Data
@Getter
@Setter
public class PersonDTO {
    private int userId;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @Email
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 2, message = "Password must be at least 2 characters long")
    private String password;

    private String passwordConfirmation;

    @NotEmpty(message = "Person credentials should not be empty")
    @Size(min = 2, max = 100, message = "Person credentials should be between 2 and 100 characters long")
    private String credentials;

    @Min(value = 1900, message = "Person birth date should not be earlier than 1900")
    private int birthDate;

    private List<Book> books;
}
