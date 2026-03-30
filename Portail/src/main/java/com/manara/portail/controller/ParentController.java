

package com.manara.portail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ParentController {

    @GetMapping({"/parent", "/parent/dashboard"})
    public String pageParent() {
        return "parent";
    }
}
