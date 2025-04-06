package ru.diasoft.library.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.diasoft.library.dto.CommentDto;
import ru.diasoft.library.service.CommentService;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentRequestDto) {
        CommentDto comment = commentService.create(commentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @GetMapping("/comment")
    public ResponseEntity<List<CommentDto>> findBookComments(@RequestParam Long bookId) {
        List<CommentDto> comments = commentService.findAllByBookId(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<List<CommentDto>> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
