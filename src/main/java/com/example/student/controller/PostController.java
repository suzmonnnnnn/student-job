package com.example.student.controller;

import com.example.student.entity.Post;
import com.example.student.entity.User;
import com.example.student.service.PostService;
import com.example.student.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping({"/new", "/create"})
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "create-post";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("post") Post post,
                         BindingResult result,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "create-post";
        try {
            User user = userService.findByUsername(authentication.getName());
            postService.create(post, imageFile, user);
            redirectAttributes.addFlashAttribute("success", "Post created successfully.");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            result.rejectValue("image", "image.invalid", e.getMessage());
            return "create-post";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, Authentication authentication) {
        Post post = postService.findById(id);
        User user = authentication == null ? null : userService.findByUsername(authentication.getName());
        model.addAttribute("post", post);
        model.addAttribute("canModify", postService.canModify(post, user));
        return "post-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        Post post = postService.findById(id);
        User user = userService.findByUsername(authentication.getName());
        if (!postService.canModify(post, user)) throw new SecurityException("You are not allowed to edit this post");
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("post") Post post,
                         BindingResult result,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "edit-post";
        try {
            User user = userService.findByUsername(authentication.getName());
            postService.update(id, post, imageFile, user);
            redirectAttributes.addFlashAttribute("success", "Post updated successfully.");
            return "redirect:/posts/" + id;
        } catch (IllegalArgumentException e) {
            result.rejectValue("image", "image.invalid", e.getMessage());
            return "edit-post";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName());
        postService.delete(id, user);
        redirectAttributes.addFlashAttribute("success", "Post deleted successfully.");
        return "redirect:/dashboard";
    }
}
