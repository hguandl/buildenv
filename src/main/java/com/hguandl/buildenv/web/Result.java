package com.hguandl.buildenv.web;

import com.hguandl.buildenv.util.Sandbox;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class Result {
    @PostMapping("/upload")
    public String result(Model model, @RequestParam(value = "code") String code) {
        Sandbox instance = new Sandbox(code);
        String result = null;
        try {
            result = instance.result();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        model.addAttribute("result", result);
        return "result";
    }
}
