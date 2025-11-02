package com.espeditomelo.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("")
    public String getAdminForm(){
        return "adminForm";
    }



}
