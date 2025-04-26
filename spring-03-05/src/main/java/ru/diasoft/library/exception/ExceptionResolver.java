package ru.diasoft.library.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.diasoft.library.dto.ErrorDto;

@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorDto> handle(AuthorNotFoundException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto("Requested author not found"));
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorDto> handle(BookNotFoundException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto("Requested book not found"));
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<ErrorDto> handle(GenreNotFoundException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto("Requested genre not found"));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorDto> handle(CommentNotFoundException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto("Requested comment not found"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handle(Exception e) throws Exception {
        if (e instanceof AuthenticationException || e instanceof AccessDeniedException) {
            throw e;
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto("Internal server exception"));
    }
}
