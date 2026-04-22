package z_project3.controller;

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

    @GetMapping("/get/{postid}")
    public ResponseEntity<?> getcomments(@PathVariable Long postid) {
        return new ResponseEntity<>(commentService.getcommentsbypostid(postid), HttpStatus.OK);
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments() {
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }
}
