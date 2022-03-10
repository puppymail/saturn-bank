package com.epam.saturn.operator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class SampleEntityController {

    @ResponseBody
    @RequestMapping("/")
    public String test() {
        return "test";
    }

}
