package pl.kniotes.elearn_course_viewer.Controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kniotes.elearn_course_viewer.Models.Course.Course;
import pl.kniotes.elearn_course_viewer.Models.Course.CourseParserJSON;
import pl.kniotes.elearn_course_viewer.Models.Path.OrderedCourse;
import pl.kniotes.elearn_course_viewer.Models.Path.Path;
import pl.kniotes.elearn_course_viewer.Models.Path.Section;
import pl.kniotes.elearn_course_viewer.Models.Path.SectionParserJSON;
import pl.kniotes.elearn_course_viewer.Repositories.CourseRepository;
import pl.kniotes.elearn_course_viewer.Repositories.PathRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        if (description != null && !description.isBlank()) {
            path.setDescription(description);
        }
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
        Path path = this.pathRepository.findPathById(id);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
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
    public Section postSection(@PathVariable("pathId") String pathId, String name, String description, Integer orderKey) {
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
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
        }
        int key = 0;
        if (orderKey != null) {
            if (orderKey < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OrderKey must be bigger than 1");
            }
            if (path.getSections().containsKey(orderKey)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is a section assigned to this orderKey");
            }
            key = orderKey;
        } else {
            for (int item : path.getSections().keySet()) {
                if (key < item) {
                    key = item;
                }
            }
            key++;
        }
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
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
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
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
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
        if (description != null && !description.isBlank()) {
            section.setDescription(description);
        }
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
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
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
    public Path changeSectionsOrder(@PathVariable("pathId") String pathId, @RequestBody String json) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
        }
        HashMap<Integer, Section> sections = new HashMap<>();
        Type placeholderType = new TypeToken<ArrayList<SectionParserJSON>>() {
        }.getType();
        List<SectionParserJSON> objects = new Gson().fromJson(json, placeholderType);
        for (SectionParserJSON item : objects) {
            if (path.getSections().get(item.getOldIndex()) == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the sections seems to not exist");
            }
            sections.put(item.getNewIndex(), path.getSections().get(item.getOldIndex()));
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
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
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
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        if (section.getCourses() == null) {
            section.setCourses(new ArrayList<>());
        }
        int key;
        if (orderKey != null) {
            if (orderKey < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OrderKey must be bigger than 0");
            }
            for (OrderedCourse item : section.getCourses()) {
                if (item.getOrderIndex() == orderKey) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is a course assigned to this orderKey");
                }
            }
            key = orderKey;
        } else {
            key = section.getCourses().size() + 1;
        }
        Course course = this.courseRepository.findCourseById(courseId);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is not set");
        }
        OrderedCourse orderedCourse = new OrderedCourse(key, course);
        section.getCourses().add(orderedCourse);
        path = this.pathRepository.save(path);
        section = path.getSections().get(sectionKey);
        OrderedCourse addedCourse = null;
        for (OrderedCourse item : section.getCourses()) {
            if (item.getOrderIndex() == key) {
                addedCourse = item;
            }
        }
        return addedCourse;
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/paths/{pathId}/sections/{sectionKey}/courses/{courseKey}")
    public Boolean deleteCourseFromSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey, @PathVariable("courseKey") Integer courseKey) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        if (section.getCourses() == null) {
            section.setCourses(new ArrayList<>());
        }
        OrderedCourse orderedCourse = null;
        for (OrderedCourse item : section.getCourses()) {
            if (item.getOrderIndex() == courseKey) {
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
    public Section changeCoursesOrderInSection(@PathVariable("pathId") String pathId, @PathVariable("sectionKey") Integer sectionKey, @RequestBody String json) {
        Path path = this.pathRepository.findPathById(pathId);
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path is invalid");
        }
        if (path.getSections() == null) {
            path.setSections(new HashMap<>());
        }
        Section section = path.getSections().get(sectionKey);
        if (section == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section is invalid");
        }
        List<OrderedCourse> courses = new ArrayList<>();
        Type placeholderType = new TypeToken<ArrayList<CourseParserJSON>>() {
        }.getType();
        List<CourseParserJSON> objects = new Gson().fromJson(json, placeholderType);
        for(CourseParserJSON item : objects) {
            if (this.courseRepository.findCourseById(item.getCourseId()) == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the courses seems to not exist");
            }
            OrderedCourse course = new OrderedCourse();
            course.setOrderIndex(item.getOrderIndex());
            course.setCourse(this.courseRepository.findCourseById(item.getCourseId()));
            courses.add(course);
        }
        section.setCourses(courses);
        this.pathRepository.save(path);
        return this.pathRepository.findPathById(pathId).getSections().get(sectionKey);
    }
}
