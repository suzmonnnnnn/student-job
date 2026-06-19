package com.example.student.service;

import com.example.student.entity.Post;
import com.example.student.entity.User;
import com.example.student.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    public PostService(PostRepository postRepository, FileStorageService fileStorageService) {
        this.postRepository = postRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Post> findAll() { return postRepository.findAllWithUserOrderByCreatedDateDesc(); }
    public Post findById(Long id) { return postRepository.findByIdWithUser(id).orElseThrow(() -> new IllegalArgumentException("Post not found")); }
    public List<Post> findByUser(User user) { return postRepository.findByCreatedByOrderByCreatedDateDesc(user); }
    public List<Post> search(String keyword) { return keyword == null || keyword.isBlank() ? findAll() : postRepository.search(keyword.trim()); }

    public Post create(Post post, MultipartFile image, User user) {
        post.setCreatedBy(user);
        post.setCreatedDate(LocalDateTime.now());
        post.setImage(fileStorageService.store(image));
        return postRepository.save(post);
    }

    public Post update(Long id, Post updated, MultipartFile image, User user) {
        Post existing = findById(id);
        ensureOwnerOrAdmin(existing, user);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setLocation(updated.getLocation());
        if (image != null && !image.isEmpty()) {
            fileStorageService.delete(existing.getImage());
            existing.setImage(fileStorageService.store(image));
        }
        return postRepository.save(existing);
    }

    public void delete(Long id, User user) {
        Post post = findById(id);
        ensureOwnerOrAdmin(post, user);
        fileStorageService.delete(post.getImage());
        postRepository.delete(post);
    }

    public boolean canModify(Post post, User user) {
        if (post == null || user == null) return false;
        boolean owner = post.getCreatedBy() != null && post.getCreatedBy().getId().equals(user.getId());
        boolean admin = "ROLE_ADMIN".equals(user.getRole());
        return owner || admin;
    }

    private void ensureOwnerOrAdmin(Post post, User user) {
        if (!canModify(post, user)) throw new SecurityException("You are not allowed to modify this post");
    }
}
