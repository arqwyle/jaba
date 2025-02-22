package com.example.lab3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorController {

    @GetMapping("/author")
    public String getAuthorInfo(Model model) {
        model.addAttribute("authorName", "Юрий Алексеевич Катохин");
        return "author";
    }
}