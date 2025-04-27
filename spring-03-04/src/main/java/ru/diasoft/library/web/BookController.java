package ru.diasoft.library.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.diasoft.library.service.BookService;
import ru.diasoft.library.dto.BookDto;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto book = bookService.create(bookDto);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookDto>> findBooks() {
        List<BookDto> books = bookService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> findBook(@PathVariable Long id) {
        BookDto book = bookService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        BookDto book = bookService.update(id, bookDto);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
