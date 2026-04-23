package z_project3.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import z_project3.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(Long postId);
    @Modifying
    @Transactional
   // @Query("DELETE FROM Comment c WHERE c.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
    long countByPostId(Long postId);
}
