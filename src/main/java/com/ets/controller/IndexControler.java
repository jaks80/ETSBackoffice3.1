package com.ets.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Yusuf
 */
@Controller
@RequestMapping("/ic")
public class IndexControler {

    @RequestMapping(value = "/index")
    public String getIndex(HttpServletRequest request) {
        System.out.println("index access............");
        return "/index";
    }
}
