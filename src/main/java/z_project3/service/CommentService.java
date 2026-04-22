package z_project3.service;

import org.hibernate.annotations.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import z_project3.model.Comment;
import z_project3.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    public CommentRepository commentRepository;
    public List<Comment> getcommentsbypostid(Long id){
        return commentRepository.findByPostId(id);
    }
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }
}
