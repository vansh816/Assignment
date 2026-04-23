//package z_project3.controller;
//
//import z_project3.model.Comment;
//import z_project3.model.Post;
//import z_project3.repository.CommentRepository;
//import z_project3.repository.PostRepository;
//import z_project3.service.CommentService;
//import z_project3.service.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class PostController {
//
//    @Autowired
//    private PostService postService;
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private PostRepository postRepository;
//
//    @PostMapping("/post")
//    public ResponseEntity<?> post(@RequestBody Post post){
//     postService.create(post);
//     return new ResponseEntity<>(HttpStatus.OK);
//    }
//    @PostMapping("/comment/{id}")
//    public ResponseEntity<?> commentpost(@RequestBody Comment comment,@PathVariable Long id){
//        Post post= postService.findById(id).orElse(null);
//        if(post==null){
//            return new ResponseEntity<>("Post Not Found ",HttpStatus.NOT_FOUND);
//        }
//        else{
//            post.getComment().add(comment);
//            postRepository.save(post);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//    }
//    @PostMapping("/like/{id}")
//    public ResponseEntity<?> commentpost(@PathVariable Long id){
//        Post post= postService.findById(id).orElse(null);
//        if(post==null){
//            return new ResponseEntity<>("Post Not Found ",HttpStatus.NOT_FOUND);
//        }
//        else{
//            post.setLikes(post.getLikes()+1);
//            postRepository.save(post);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//    }
//    @GetMapping("/get")
//    public ResponseEntity<?> getallposts(){
//        return  new ResponseEntity<>(postService.getposts(),HttpStatus.OK);
//    }
//}
package z_project3.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import z_project3.model.Comment;
import z_project3.model.Post;
import z_project3.repository.CommentRepository;
import z_project3.repository.PostRepository;
import z_project3.service.NotificationService;
import z_project3.service.NotificationSweeper;
import z_project3.service.PostService;
import z_project3.service.RedisService;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RedisService redisService;
    @Autowired
    private NotificationService notificationService; // Top pe add kiya
    @Autowired
    CommentRepository commentRepository;

    @Operation(summary = "Create a new post")
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        postService.create(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get all Posts")
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
        return new ResponseEntity<>(postService.getposts(), HttpStatus.OK);
    }

    @Operation(summary = "Comment by id")
    @PostMapping("/comment/{id}")
    public ResponseEntity<?> addComment(@RequestBody Comment comment, @PathVariable Long id) {
        Post post = postService.findById(id).orElse(null);
        if (post == null) {
            return new ResponseEntity<>("Post Not Found", HttpStatus.NOT_FOUND);
        }
        // Vertical Cap
        if (redisService.isDepthCapReached(comment.getDepthLevel())) {
            return new ResponseEntity<>("Depth limit exceeded max 20", HttpStatus.TOO_MANY_REQUESTS);
        }
        if ("BOT".equals(comment.getAuthorType())) {
            // Horizontal Cap
            if (redisService.isBotCapReached(id)) {
                return new ResponseEntity<>("Bot reply limit exceeded max 100", HttpStatus.TOO_MANY_REQUESTS);
            }
            // Cooldown Cap
            if (redisService.isCooldownActive(comment.getAuthorId(), post.getAuthorId())) {
                return new ResponseEntity<>("Cooldown active wait 10 minutes", HttpStatus.TOO_MANY_REQUESTS);
            }
            redisService.updateViralityScore(id, "BOT_REPLY");
           // Comment wale block mein, saare checks ke baad:
            String msg = "Bot " + comment.getAuthorId() + " replied to your post";
            notificationService.handleBotNotification(post.getAuthorId(), msg);
        } else {
            redisService.updateViralityScore(id, "HUMAN_COMMENT");
        }
        comment.setPostId(id);
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Like by id")
    @PostMapping("/like/{id}")
    public ResponseEntity<?> likePost(@PathVariable Long id) {
        Post post = postService.findById(id).orElse(null);
        if (post == null) {
            return new ResponseEntity<>("Post Not Found", HttpStatus.NOT_FOUND);
        }
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
        redisService.updateViralityScore(id, "HUMAN_LIKE");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/post/{id}/virality")
    public ResponseEntity<?> getViralityScore(@PathVariable Long id) {
        String score = redisService.getViralityScore(id);
        return new ResponseEntity<>("Virality Score: " + score, HttpStatus.OK);
    }

    @Operation(summary = "Delete post by id")
    @DeleteMapping("/comments/delete/{postId}")
    public ResponseEntity<?> deleteComments(@PathVariable Long postId) {
        commentRepository.deleteByPostId(postId);
        return new ResponseEntity<>("Deleted!", HttpStatus.OK);
    }

    @Operation(summary = "Counts comments by postid")
    @GetMapping("/comments/count/{postId}")
    public ResponseEntity<?> getCommentCount(@PathVariable Long postId) {
        long count = commentRepository.countByPostId(postId);
        return new ResponseEntity<>("Total comments: " + count, HttpStatus.OK);
    }

}
