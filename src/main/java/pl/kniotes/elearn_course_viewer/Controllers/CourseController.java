package pl.kniotes.elearn_course_viewer.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kniotes.elearn_course_viewer.Models.Course.Course;
import pl.kniotes.elearn_course_viewer.Models.Course.OrderedModule;
import pl.kniotes.elearn_course_viewer.Models.Module.Module;
import pl.kniotes.elearn_course_viewer.Repositories.CourseRepository;
import pl.kniotes.elearn_course_viewer.Repositories.ModuleRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @CrossOrigin("*")
    @GetMapping("/api/courses")
    public List<Course> getCourses() {
        return this.courseRepository.findAll();
    }

    @CrossOrigin("*")
    @PostMapping("/api/courses")
    public Course postCourse(String name, String description) {
        Course course = new Course();
        if (name != null && !name.isBlank()) {
            course.setName(name);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not set");
        }
        course.setDescription(description);
        return this.courseRepository.save(course);
    }

    @CrossOrigin("*")
    @GetMapping("/api/courses/{id}")
    public Course getCourse(@PathVariable("id") String id) {
        return this.courseRepository.findCourseById(id);
    }

    @CrossOrigin("*")
    @PutMapping("/api/courses/{id}")
    public Course updateCourse(@PathVariable("id") String id, String name, String description, String instructor, String exam) {
        Course course = this.courseRepository.findCourseById(id);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is invalid");
        }
        if (name != null && !name.isBlank()) {
            course.setName(name);
        } else {
            if (course.getName() == null || course.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course name seems to be empty. Set the name in order to update");
            }
        }
        if (description != null && !description.isBlank()) {
            course.setDescription(description);
        }
        if (instructor != null && !instructor.isBlank()) {
            course.setInstructor(instructor);
        }
        if (exam != null && !exam.isBlank()) {
            course.setExam(exam);
        }
        return this.courseRepository.save(course);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/courses/{id}")
    public Boolean deleteCourse(@PathVariable("id") String id) {
        this.courseRepository.deleteCourseById(id);
        return this.courseRepository.findCourseById(id) == null;
    }

    @CrossOrigin("*")
    @GetMapping("/api/courses/{courseId}/modules")
    public List<OrderedModule> getCourseModules(@PathVariable("courseId") String courseId) {
        Course course = this.courseRepository.findCourseById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is invalid");
        }
        return course.getModules();
    }

    @CrossOrigin("*")
    @PostMapping("/api/courses/{courseId}/modules")
    public OrderedModule postCourseModule(@PathVariable("courseId") String courseId, String moduleId, Integer orderKey) {
        Course course = this.courseRepository.findCourseById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is invalid");
        }
        int key;
        if (orderKey != null && !orderKey.toString().isBlank()) {
            key = orderKey;
        } else {
            key = course.getModules().size();
        }
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is not set");
        }
        OrderedModule orderedModule = new OrderedModule(key, module);
        course.getModules().add(orderedModule);
        this.courseRepository.save(course);
        return this.courseRepository.findCourseById(courseId).getModules().get(key);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/courses/{courseId}/modules/{moduleKey}")
    public Boolean deleteModuleFromCourse(@PathVariable("courseId") String courseId, @PathVariable("moduleKey") int moduleKey) {
        Course course = this.courseRepository.findCourseById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is invalid");
        }
        OrderedModule orderedModule = null;
        for (OrderedModule item : course.getModules()) {
            if (item.getOrderIndex() == moduleKey) {
                orderedModule = item;
            }
        }
        if (orderedModule == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is not set");
        }
        boolean removed = course.getModules().remove(orderedModule);
        this.courseRepository.save(course);
        return removed;
    }

    @CrossOrigin("*")
    @PutMapping("/api/courses/{courseId}/modules")
    public Course changeModulesOrderInCourse(@PathVariable("courseId") String courseId, List<OrderedModule> modules) {
        Course course = this.courseRepository.findCourseById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is invalid");
        }
        course.setModules(modules);
        return this.courseRepository.save(course);
    }
}
