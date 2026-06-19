package com.example.student.repository;

import com.example.student.entity.Post;
import com.example.student.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedDateDesc();
    List<Post> findByCreatedByOrderByCreatedDateDesc(User user);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.createdBy WHERE p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.createdBy WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(p.location) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "ORDER BY p.createdDate DESC")
    List<Post> search(@Param("kw") String keyword);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.createdBy ORDER BY p.createdDate DESC")
    List<Post> findAllWithUserOrderByCreatedDateDesc();
}
