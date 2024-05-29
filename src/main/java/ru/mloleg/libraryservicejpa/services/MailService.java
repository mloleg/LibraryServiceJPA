package ru.mloleg.libraryservicejpa.services;

import ru.mloleg.libraryservicejpa.models.Person;

public interface MailService {
    void sendConfirmationCode(Person person);
}
