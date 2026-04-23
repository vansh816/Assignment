package z_project3.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import z_project3.model.Bot;
import z_project3.model.Post;
import z_project3.repository.CommentRepository;
import z_project3.service.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    public CommentService commentService;

    @Operation(summary = "Create a new Comment")
    @GetMapping("/get/{postid}")
    public ResponseEntity<?> getcomments(@PathVariable Long postid) {
        return new ResponseEntity<>(commentService.getcommentsbypostid(postid), HttpStatus.OK);
    }

    @Operation(summary = "Get all Comments")
    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments() {
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }
}
