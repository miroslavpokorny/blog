package io.github.miroslavpokorny.blog.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    @RequestMapping("/admin")
    public String administrationRedirect(){
        return "redirect:/admin/";
    }

    @RequestMapping("/admin/")
    public String administration(){
        return "admin";
    }
}
