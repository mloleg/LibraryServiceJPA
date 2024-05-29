package ru.mloleg.libraryservicejpa.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mloleg.libraryservicejpa.dto.BookDTO;
import ru.mloleg.libraryservicejpa.dto.PersonDTO;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.services.BooksService;
import ru.mloleg.libraryservicejpa.services.PeopleService;
import ru.mloleg.libraryservicejpa.services.S3Service;
import ru.mloleg.libraryservicejpa.util.mappers.BookMapper;
import ru.mloleg.libraryservicejpa.util.mappers.PersonMapper;

import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    private final S3Service s3Service;

    private final PersonMapper personMapper;
    private final BookMapper bookMapper;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, S3Service s3Service,
                           PersonMapper personMapper, BookMapper bookMapper) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.s3Service = s3Service;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {

        if (Objects.isNull(page) || Objects.isNull(booksPerPage)) {
            model.addAttribute("books",
                    booksService.getAll()
                            .stream()
                            .map(bookMapper::toDTO)
                            .collect(Collectors.toList()));
        } else {
            model.addAttribute("books",
                    booksService.getWithPagination(page, booksPerPage, sortByYear)
                            .stream()
                            .map(bookMapper::toDTO)
                            .collect(Collectors.toList()));
        }

        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new BookDTO());
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookMapper.toDTO(booksService.getById(id)));

        return "books/edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") PersonDTO personDTO, Model model) {
        model.addAttribute("book", bookMapper.toDTO(booksService.getById(id)));

        Person owner = booksService.getById(id).getOwner();

        if (!Objects.isNull(owner)) {
            model.addAttribute("bookOwner", personMapper.toDTO(owner));
        } else {
            model.addAttribute("people", peopleService.getAll()
                    .stream()
                    .map(personMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return "books/show";
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid BookDTO bookDTO, BindingResult bindingResult,
                         @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookDTO.setImage(s3Service.uploadFile(file));
        booksService.create(bookMapper.toEntity(bookDTO));

        return "redirect:/books";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books",
                booksService.getByTitle(query)
                        .stream()
                        .map(bookMapper::toDTO)
                        .collect(Collectors.toList()));

        return "books/search";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid BookDTO bookDTO,
                         BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        s3Service.deleteFile(booksService.getById(id).getImage());
        bookDTO.setImage(s3Service.uploadFile(file));
        booksService.update(id, bookMapper.toEntity(bookDTO));

        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);

        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") PersonDTO personDTO) {
        booksService.assign(id, personMapper.toEntity(personDTO));

        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        s3Service.deleteFile(booksService.getById(id).getImage());
        booksService.delete(id);

        return "redirect:/books";
    }
}
