package ru.mloleg.libraryservicejpa.services;

import ru.mloleg.libraryservicejpa.models.Person;

public interface RegistrationService {
    void register(Person person);

    boolean activatePerson(String code);
}
