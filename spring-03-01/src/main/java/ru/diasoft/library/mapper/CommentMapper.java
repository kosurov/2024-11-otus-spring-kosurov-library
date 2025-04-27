package ru.diasoft.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "book.id", target = "bookId")
    CommentDto commentToCommentDto(Comment comment);
}
