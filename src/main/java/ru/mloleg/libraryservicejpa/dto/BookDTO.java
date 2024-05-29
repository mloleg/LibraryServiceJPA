package ru.mloleg.libraryservicejpa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.mloleg.libraryservicejpa.models.Person;

@Data
@Getter
@Setter
public class BookDTO {
    private int bookId;

    private Person owner;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters long")
    private String title;

    @NotEmpty(message = "Author name should not be empty")
    @Size(min = 2, max = 100, message = "Author name should be between 2 and 100 characters long")
    private String author;

    @Min(value = 1500, message = "Year of publication should not be earlier than 1500")
    private int publicationYear;

    private String image;
}
