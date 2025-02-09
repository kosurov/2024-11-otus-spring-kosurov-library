package ru.diasoft.library.exception;

import org.springframework.shell.command.CommandExceptionResolver;
import org.springframework.shell.command.CommandHandlingResult;
import org.springframework.stereotype.Component;

@Component
public class ExceptionResolver implements CommandExceptionResolver {

    @Override
    public CommandHandlingResult resolve(Exception e) {
        if (e instanceof AuthorNotFoundException) {
            return CommandHandlingResult.of("Requested author not found\n");
        }
        if (e instanceof BookNotFoundException) {
            return CommandHandlingResult.of("Requested book not found\n");
        }
        if (e instanceof GenreNotFoundException) {
            return CommandHandlingResult.of("Requested genre not found\n");
        }
        if (e instanceof CommentNotFoundException) {
            return CommandHandlingResult.of("Requested comment not found\n");
        }
        return CommandHandlingResult.of("Internal server exception\n");
    }
}
