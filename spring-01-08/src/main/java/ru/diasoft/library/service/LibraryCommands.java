package ru.diasoft.library.service;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.dto.BookRequestDto;

import java.util.stream.Collectors;

@Command(group = "Library Commands")
public class LibraryCommands {

    private final BookService bookService;

    public LibraryCommands(BookService bookService) {
        this.bookService = bookService;
    }

    @Command(command = "create-book", description = "Add a new book to the Library")
    public Book createBook(@Option(longNames = "title", required = true) String title,
                           @Option(longNames = "author-full-name", required = true) String authorFullName,
                           @Option(longNames = "genre", required = true) String genre) {
        BookRequestDto bookRequestDto = BookRequestDto.builder()
                .title(title)
                .authorFullName(authorFullName)
                .genre(genre)
                .build();
        return bookService.create(bookRequestDto);
    }

    @Command(command = "find-book", description = "Find a book in the Library by id")
    public Book findBookById(long id) {
        return bookService.getById(id);
    }

    @Command(command = "find-books", description = "List all books in the Library")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(Book::toString)
                .collect(Collectors.joining("\n"));
    }

    @Command(command = "update-book", description = "Update ad existing book in the Library")
    public Book updateBook(@Option(longNames = "id", required = true) long id,
                           @Option(longNames = "title", required = true) String title,
                           @Option(longNames = "author-full-name", required = true) String authorFullName,
                           @Option(longNames = "genre", required = true) String genre) {
        BookRequestDto bookRequestDto = BookRequestDto.builder()
                .id(id)
                .title(title)
                .authorFullName(authorFullName)
                .genre(genre)
                .build();
        return bookService.update(bookRequestDto);
    }

    @Command(command = "delete-book", description = "Remove a book from the Library by id")
    public void deleteBookById(long id) {
        bookService.delete(id);
    }
}
