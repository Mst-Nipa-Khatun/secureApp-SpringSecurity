package com.example.secureapp.controller;


import com.example.secureapp.entity.Post;
import com.example.secureapp.repository.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "posts";
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return "createPost";
    }
    @PostMapping("/create")
    public String createPost(@RequestParam String title, @RequestParam String content, RedirectAttributes redirectAttrs) {
        if (title == null || title.isEmpty() || content == null || content.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Title and Content cannot be empty");
            return "redirect:/posts/create";
        }

        // Strip script tags
        title = title.replaceAll("<script>", "").replaceAll("</script>", "");
        content = content.replaceAll("<script>", "").replaceAll("</script>", "");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        postRepository.save(new Post(title, content, username));
        return "redirect:/posts";
    }

}
