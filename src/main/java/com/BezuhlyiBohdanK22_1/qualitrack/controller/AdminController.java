package com.BezuhlyiBohdanK22_1.qualitrack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    @GetMapping("/dashboard")
    public String dashboard() {
        logger.info("Dashboard Admin");
        return "adminDashboard";
    }
    @PostMapping("/lecturers/create")
    public void createLecturer(@RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String faculty) {
        logger.info("Create Lecturer");

    }
}
