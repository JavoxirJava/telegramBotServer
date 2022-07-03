package it.ca.telegrambotserver.telegramBot.bot.controller;

import it.ca.telegrambotserver.telegramBot.bot.payload.ResCourse;
import it.ca.telegrambotserver.telegramBot.bot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping
    public String getCourse() {
        return "course";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ResCourse> getCourseList() {
        return courseService.courseList();
    }

    @PostMapping("/add")
    @ResponseBody
    public void addCourse(@RequestBody ResCourse resCourse) {
        courseService.createCourse(resCourse);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public void editCourse(@PathVariable Integer id, @RequestBody ResCourse resCourse) {
        courseService.editCourse(id, resCourse);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
    }
}