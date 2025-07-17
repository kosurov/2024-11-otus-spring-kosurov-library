package ru.diasoft.library.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.diasoft.library.service.BookService;

@Component
public class BookCountHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    public BookCountHealthIndicator(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public Health health() {
        int bookCount = bookService.findAll().size();
        String bookCountDetailKey = "books";
        int minBookCount = 1;
        if (bookCount < minBookCount) {
            return Health.down()
                    .withDetail(bookCountDetailKey, bookCount)
                    .build();
        }
        return Health.up()
                .withDetail(bookCountDetailKey, bookCount)
                .build();
    }
}
