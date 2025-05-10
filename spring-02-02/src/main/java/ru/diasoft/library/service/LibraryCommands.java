package ru.diasoft.library.service;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import ru.diasoft.library.domain.Book;
import ru.diasoft.library.domain.Comment;
import ru.diasoft.library.dto.BookRequestDto;
import ru.diasoft.library.dto.CommentDto;

import java.util.stream.Collectors;

@Command(group = "Library Commands")
public class LibraryCommands {

    private final BookService bookService;
    private final CommentService commentService;

    public LibraryCommands(BookService bookService, CommentService commentService) {
        this.bookService = bookService;
        this.commentService = commentService;
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

    @Command(command = "create-comment", description = "Add a new comment for a book")
    public Comment createComment(@Option(longNames = "bookId", required = true) Long bookId,
                                 @Option(longNames = "text", required = true) String text) {
        CommentDto commentDto = CommentDto.builder()
                .bookId(bookId)
                .text(text).build();
        return commentService.create(commentDto);
    }

    @Command(command = "delete-comment", description = "Remove a comment for a book by id")
    public void deleteCommentById(long id) {
        commentService.delete(id);
    }

    @Command(command = "find-book-comments", description = "List all comments for a book by bookId")
    public String findBookComments(long bookId) {
        return commentService.findAllByBookId(bookId).stream()
                .map(CommentDto::toString)
                .collect(Collectors.joining("\n"));
    }

}
