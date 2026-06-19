package com.example.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Convenience routes so users can use /jobs/new for creating a job post. */
@Controller
public class JobController {
    @GetMapping({"/jobs/new", "/jobs/create"})
    public String newJob() {
        return "redirect:/posts/new";
    }

    @GetMapping("/jobs")
    public String jobs() {
        return "redirect:/search?q=Job";
    }
}
