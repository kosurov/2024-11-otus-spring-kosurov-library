package ru.diasoft.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.dto.BookDto;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "author.fullName", target = "author")
    @Mapping(source = "genre.name", target = "genre")
    BookDto bookToBookDto(Book book);
}
