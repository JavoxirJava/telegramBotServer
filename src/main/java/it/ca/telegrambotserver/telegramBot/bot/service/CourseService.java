package it.ca.telegrambotserver.telegramBot.bot.service;

import it.ca.telegrambotserver.telegramBot.bot.entity.Course;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResCourse;
import it.ca.telegrambotserver.telegramBot.bot.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public void createCourse(ResCourse resCourse) {
        if (!courseRepository.existsByNameIgnoreCase(resCourse.getName())) {
            Course course = new Course();
            course.setName(resCourse.getName());
            course.setInfo(resCourse.getInfo());
            courseRepository.save(course);
        }
    }

    public List<ResCourse> courseList() {
        java.util.List<Course> all = courseRepository.findAll();
        List<ResCourse> courses = new ArrayList<>();
        for (Course course : all) {
            ResCourse resCourse = new ResCourse();
            resCourse.setName(course.getName());
            resCourse.setInfo(course.getInfo());
            courses.add(resCourse);
        }
        return courses;
    }

    public void editCourse(Integer id, ResCourse resCourse) {
        Optional<Course> byId = courseRepository.findById(id);
        if (byId.isPresent()) {
            Course course = byId.get();
            course.setName(resCourse.getName());
            course.setInfo(resCourse.getInfo());
            courseRepository.save(course);
        }
    }

    public boolean deleteBotCourse(String name) {
        try {
            deleteCourse(courseRepository.findByName(name).getId());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void deleteCourse(Integer id) {
        if (courseRepository.findById(id).isPresent()) courseRepository.deleteById(id);
    }


    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
}
