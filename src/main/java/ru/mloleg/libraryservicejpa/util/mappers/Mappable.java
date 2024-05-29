package ru.mloleg.libraryservicejpa.util.mappers;

public interface Mappable<E, D> {

    D toDTO(E entity);

    E toEntity(D dto);

}