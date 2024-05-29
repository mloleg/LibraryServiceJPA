package ru.mloleg.libraryservicejpa.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mloleg.libraryservicejpa.dto.PersonDTO;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.services.PeopleService;
import ru.mloleg.libraryservicejpa.util.mappers.PersonMapper;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;

    private final PersonMapper personMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonMapper personMapper) {
        this.peopleService = peopleService;
        this.personMapper = personMapper;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.getAll());

        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.getById(id));
        model.addAttribute("books", peopleService.getBooksByUserId(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new PersonDTO());

        return "people/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.getById(id));

        return "people/edit";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        Person person = personMapper.toEntity(personDTO);

        peopleService.create(person);

        return "redirect:/people";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid PersonDTO personDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }

        Person person = personMapper.toEntity(personDTO);

        peopleService.update(id, person);

        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);

        return "redirect:/people";
    }
}
