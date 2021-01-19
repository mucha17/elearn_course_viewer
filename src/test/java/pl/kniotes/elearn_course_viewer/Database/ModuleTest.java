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
import pl.kniotes.elearn_course_viewer.Models.Module.Content;
import pl.kniotes.elearn_course_viewer.Models.Module.ContentType;
import pl.kniotes.elearn_course_viewer.Models.Module.Lesson;
import pl.kniotes.elearn_course_viewer.Models.Module.Module;
import pl.kniotes.elearn_course_viewer.Repositories.ModuleRepository;

import java.util.HashMap;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(DatabaseTestConfiguration.class)
public class ModuleTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ModuleRepository moduleRepository;

    @BeforeEach
    public void beforeEach() {
        this.mongoTemplate.indexOps(Module.class).ensureIndex(new Index().on("name", Sort.Direction.ASC));
    }

    @AfterEach
    public void afterEach() {
        this.mongoTemplate.dropCollection(Module.class);
    }

    @Test
    public void addModule() {
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
        Module response = this.moduleRepository.save(module);
        Assertions.assertNotNull(response);
    }
}
