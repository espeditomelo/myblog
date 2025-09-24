package com.espeditomelo.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/admin")
    public String getAdminForm(){
        return "adminForm";
    }


}
