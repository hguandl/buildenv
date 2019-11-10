package com.hguandl.buildenv.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Editor {

    @GetMapping("/")
    public String editor() {
        return "editor";
    }
}
