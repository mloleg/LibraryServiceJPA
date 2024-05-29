package ru.mloleg.libraryservicejpa.util.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mloleg.libraryservicejpa.dto.BookDTO;
import ru.mloleg.libraryservicejpa.models.Book;
import ru.mloleg.libraryservicejpa.util.mappers.BookMapper;

@Component
public class BookMapperImpl implements BookMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public BookMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDTO toDTO(Book entity) {
        return modelMapper.map(entity, BookDTO.class);
    }

    @Override
    public Book toEntity(BookDTO dto) {
        return modelMapper.map(dto, Book.class);
    }
}
