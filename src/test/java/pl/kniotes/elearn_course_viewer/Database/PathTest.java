package pl.kniotes.elearn_course_viewer.Database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.kniotes.elearn_course_viewer.DatabaseTestConfiguration;
import pl.kniotes.elearn_course_viewer.Models.Course.Course;
import pl.kniotes.elearn_course_viewer.Models.Course.OrderedModule;
import pl.kniotes.elearn_course_viewer.Models.Module.Content;
import pl.kniotes.elearn_course_viewer.Models.Module.ContentType;
import pl.kniotes.elearn_course_viewer.Models.Module.Lesson;
import pl.kniotes.elearn_course_viewer.Models.Module.Module;
import pl.kniotes.elearn_course_viewer.Models.Path.OrderedCourse;
import pl.kniotes.elearn_course_viewer.Models.Path.Path;
import pl.kniotes.elearn_course_viewer.Models.Path.Section;
import pl.kniotes.elearn_course_viewer.Repositories.CourseRepository;
import pl.kniotes.elearn_course_viewer.Repositories.ModuleRepository;
import pl.kniotes.elearn_course_viewer.Repositories.PathRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(DatabaseTestConfiguration.class)
public class PathTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PathRepository pathRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @BeforeEach
    public void beforeEach() {
        this.mongoTemplate.indexOps(Module.class).ensureIndex(new Index().on("name", Sort.Direction.ASC));
        this.mongoTemplate.indexOps(Course.class).ensureIndex(new Index().on("name", Sort.Direction.ASC));
        this.mongoTemplate.indexOps(Path.class).ensureIndex(new Index().on("name", Sort.Direction.ASC));

        Module module = new Module();
        module.setName("Wprowadzenie do Spring MVC");
        module.setDescription("Omówienie podstaw Spring MVC i przygotowanie środowiska do pracy z nowym projektem aplikacji webowej");
        HashMap<Integer, Lesson> lessons = new HashMap<>();
        HashMap<Integer, Content> contents = new HashMap<>();
        contents.put(1, new Content(ContentType.VIDEO, "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"));
        lessons.put(1, new Lesson("Utworzenie projektu", "Opis lekcji", contents));
        module.setLessons(lessons);
        HashMap<Integer, String> downloadables = new HashMap<>();
        downloadables.put(1, "http://test.local/1");
        downloadables.put(2, "http://test.local/2");
        downloadables.put(3, "http://test.local/3");
        module.setDownloadables(downloadables);
        this.moduleRepository.save(module);

        Course course = new Course();
        course.setName("Spring MVC");
        course.setDescription("Kurs dotyczący Spring MVC");
        List<Module> allModules = this.moduleRepository.findAll();
        List<OrderedModule> courseModules = new ArrayList<>();
        for(int i = 0; i < allModules.size(); i++) {
            courseModules.add(new OrderedModule(i+1, allModules.get(i)));
        }
        course.setModules(courseModules);
        course.setExam("http://examiner.net/1");
        course.setInstructor("ID123123123asdada");
        this.courseRepository.save(course);

    }

    @AfterEach
    public void afterEach() {
        this.mongoTemplate.dropCollection(Path.class);
        this.mongoTemplate.dropCollection(Course.class);
        this.mongoTemplate.dropCollection(Module.class);
    }

    @Test
    public void addPath() {
        Section section1 = new Section();
        section1.setName("Dla początkujących");
        section1.setDescription("Sekcja zawierające kursy przeznaczone dla rozpoczynających naukę Spring");
        List<Course> courses = this.courseRepository.findAll();
        List<OrderedCourse> orderedCourses = new ArrayList<>();
        for(int i = 0; i < courses.size(); i++) {
            orderedCourses.add(new OrderedCourse(i+1, courses.get(i)));
        }
        section1.setCourses(orderedCourses);

        Section section2 = new Section();
        section2.setName("Dla zaawansowanych");
        section2.setDescription("Sekcja zawierające kursy przeznaczone dla kontynuujących naukę Spring");
        section2.setCourses(orderedCourses);

        HashMap<Integer, Section> sections = new HashMap<>();
        sections.put(1, section1);
        sections.put(2, section2);

        Path path = new Path();
        path.setName("Spring");
        path.setDescription("Ścieżka nauki Springa");
        path.setSections(sections);
        Path result = this.pathRepository.save(path);
        Assertions.assertNotNull(result);
    }
}
