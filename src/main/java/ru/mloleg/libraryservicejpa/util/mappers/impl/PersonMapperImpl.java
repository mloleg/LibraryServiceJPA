package ru.mloleg.libraryservicejpa.util.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mloleg.libraryservicejpa.dto.PersonDTO;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.util.mappers.PersonMapper;

@Component
public class PersonMapperImpl implements PersonMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PersonMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PersonDTO toDTO(Person entity) {
        return modelMapper.map(entity, PersonDTO.class);
    }

    @Override
    public Person toEntity(PersonDTO dto) {
        return modelMapper.map(dto, Person.class);
    }
}
