package pl.kniotes.elearn_course_viewer.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kniotes.elearn_course_viewer.Models.Course.Course;
import pl.kniotes.elearn_course_viewer.Models.Path.OrderedCourse;
import pl.kniotes.elearn_course_viewer.Models.Path.Path;
import pl.kniotes.elearn_course_viewer.Models.Path.Section;
import pl.kniotes.elearn_course_viewer.Repositories.CourseRepository;
import pl.kniotes.elearn_course_viewer.Repositories.PathRepository;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PathController {

    @Autowired
    private PathRepository pathRepository;

    @Autowired
    private CourseRepository courseRepository;

    @CrossOrigin("*")
    @GetMapping("/api/paths")
    public List<Path> getPaths() {
        return this.pathRepository.findAll();
    }

    @CrossOrigin("*")
    @PostMapping("/api/paths")
    public Path postPath(String name, String description) {
        Path path = new Path();
        if (name != null && !name.isBlank()) {
            path.setName(name);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not set");
        }
        path.setDescription(description);
        return this.pathRepository.save(path);
    }

    @CrossOrigin("*")
    @GetMapping("/api/paths/{id}")
    public Path getPath(@PathVariable("id") String id) {
        return this.pathRepository.findPathById(id);
    }

    @CrossOrigin("*")
    @PutMapping("/api/paths/{id}")
    public Path updatePath(@PathVariable("id") String id, String name, String description) {
        Path path = this.pathRepository.findPathById(id);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        if (name != null && !name.isBlank()) {
            path.setName(name);
        } else {
            if (path.getName() == null || path.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path name seems to be empty. Set the name in order to update");
            }
        }
        if (description != null && !description.isBlank()) {
            path.setDescription(description);
        }

        return this.pathRepository.save(path);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/paths/{id}")
    public Boolean deletePath(@PathVariable("id") String id) {
        this.pathRepository.deletePathById(id);
        return this.courseRepository.findCourseById(id) == null;
    }

    @CrossOrigin("*")
    @GetMapping("/api/paths/{pathId}/sections")
    public HashMap<Integer, Section> getSections(@PathVariable("pathId") String pathId) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        return path.getSections();
    }

    @CrossOrigin("*")
    @PostMapping("/api/paths/{pathId}/sections")
    public Section postSection(@PathVariable("pathId") String pathId, String name, String description) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        Section section = new Section();
        if (name != null && !name.isBlank()) {
            section.setName(name);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not set");
        }
        if (description != null && !description.isBlank()) {
            section.setDescription(description);
        }
        Integer key = path.getSections().size();
        path.getSections().put(key, section);
        this.pathRepository.save(path);
        return this.pathRepository.findPathById(pathId).getSections().get(key);
    }

    @CrossOrigin("*")
    @GetMapping("/api/paths/{pathId}/sections/{sectionKey}")
    public Section getSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        return path.getSections().get(sectionKey);
    }

    @CrossOrigin("*")
    @PutMapping("/api/paths/{pathId}/sections/{sectionKey}")
    public Section updateSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey, String name, String description) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        if (name != null && !name.isBlank()) {
            section.setName(name);
        } else {
            if (section.getName() == null || section.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section name seems to be empty. Set the name in order to update");
            }
        }
        section.setDescription(description);
        this.pathRepository.save(path);
        return this.pathRepository.findPathById(pathId).getSections().get(sectionKey);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/paths/{pathId}/sections/{sectionKey}")
    public Boolean deleteSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        if (path.getSections().get(sectionKey) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        path.getSections().remove(sectionKey);
        this.pathRepository.save(path);
        return this.pathRepository.findPathById(pathId).getSections().get(sectionKey) == null;
    }

    @CrossOrigin("*")
    @PutMapping("/api/paths/{pathId}/sections")
    public Path changeSectionsOrder(@PathVariable("pathId") String pathId, HashMap<Integer, Section> sections) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        path.setSections(sections);
        return this.pathRepository.save(path);
    }

    @CrossOrigin("*")
    @GetMapping("/api/paths/{pathId}/sections/{sectionKey}/courses")
    public List<OrderedCourse> getSectionCourses(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        return section.getCourses();
    }

    @CrossOrigin("*")
    @PostMapping("/api/paths/{pathId}/sections/{sectionKey}/courses")
    public OrderedCourse postCourseInSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey, String courseId, Integer orderKey) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        int key;
        if (orderKey != null && !orderKey.toString().isBlank()) {
            key = orderKey;
        } else {
            key = section.getCourses().size();
        }
        Course course = this.courseRepository.findCourseById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is not set");
        }
        OrderedCourse orderedCourse = new OrderedCourse(key, course);
        section.getCourses().add(orderedCourse);
        this.pathRepository.save(path);
        return this.pathRepository.findPathById(pathId).getSections().get(sectionKey).getCourses().get(key);
    }

//    @CrossOrigin("*")
//    @GetMapping("/api/paths/{pathId}/sections/{sectionKey}/courses/{courseKey}")
//    public OrderedCourse getCourseInSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey, @PathVariable("courseKey") Integer courseKey) {
//        Path path = this.pathRepository.findPathById(pathId);
//        if (path == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
//        }
//        Section section = path.getSections().get(sectionKey);
//        if (section == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
//        }
//        return section.getCourses().get(courseKey);
//    }

    @CrossOrigin("*")
    @DeleteMapping("/api/paths/{pathId}/sections/{sectionKey}/courses/{courseKey}")
    public Boolean deleteCourseFromSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey, @PathVariable("courseKey") Integer courseKey) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        OrderedCourse orderedCourse = null;
        for(OrderedCourse item : section.getCourses()) {
            if(item.getOrderIndex() == courseKey) {
                orderedCourse = item;
            }
        }
        if (orderedCourse == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is not set");
        }
        boolean removed = section.getCourses().remove(orderedCourse);
        this.pathRepository.save(path);
        return removed;
    }

    @CrossOrigin("*")
    @PutMapping("/api/paths/{pathId}/sections/{sectionKey}/courses")
    public Section changeCoursesOrderInSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey, List<OrderedCourse> courses) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        section.setCourses(courses);
        this.pathRepository.save(path);
        return this.pathRepository.findPathById(pathId).getSections().get(sectionKey);
    }
}
