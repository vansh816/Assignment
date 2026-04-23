package z_project3.service;

import z_project3.model.Comment;
import z_project3.model.Post;
import z_project3.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post create(Post post){
        return postRepository.save(post);
    }
    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }
    public List<Post> getposts(){
        return  postRepository.findAll();
    }
}
