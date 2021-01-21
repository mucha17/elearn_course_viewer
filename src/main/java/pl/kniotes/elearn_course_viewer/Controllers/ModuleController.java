package pl.kniotes.elearn_course_viewer.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kniotes.elearn_course_viewer.Models.Module.Content;
import pl.kniotes.elearn_course_viewer.Models.Module.ContentType;
import pl.kniotes.elearn_course_viewer.Models.Module.Lesson;
import pl.kniotes.elearn_course_viewer.Models.Module.Module;
import pl.kniotes.elearn_course_viewer.Repositories.ModuleRepository;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ModuleController {

    @Autowired
    private ModuleRepository moduleRepository;

    @CrossOrigin("*")
    @GetMapping("/api/modules")
    public List<Module> getModules() {
        return this.moduleRepository.findAll();
    }

    @CrossOrigin("*")
    @PostMapping("/api/modules")
    public Module postModule(String name, String description) {
        Module module = new Module();
        module.setName(name);
        module.setDescription(description);
        return this.moduleRepository.save(module);
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{id}")
    public Module getModule(@PathVariable("id") String id) {
        return this.moduleRepository.findModuleById(id);
    }

    @CrossOrigin("*")
    @PutMapping("/api/modules/{id}")
    public Module updateModule(@PathVariable("id") String id, String name, String description) {
        Module module = this.moduleRepository.findModuleById(id);
        if (name != null && !name.isBlank()) {
            module.setName(name);
        }
        if (description != null && !description.isBlank()) {
            module.setDescription(description);
        }
        return this.moduleRepository.save(module);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/modules/{id}")
    public Boolean deleteModule(@PathVariable("id") String id) {
        this.moduleRepository.deleteModuleById(id);
        return this.moduleRepository.findModuleById(id) == null;
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{moduleId}/lessons")
    public HashMap<Integer, Lesson> getLessons(@PathVariable("moduleId") String moduleId) {
        return this.moduleRepository.findModuleById(moduleId).getLessons();
    }

    @CrossOrigin("*")
    @PostMapping("/api/modules/{moduleId}/lessons")
    public Lesson postLesson(@PathVariable("moduleId") String moduleId, String name, String description) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        Integer key = module.getLessons().size();
        module.getLessons().put(key, new Lesson(name, description));
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(key);
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{moduleId}/lessons/{lessonId}")
    public Lesson getLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId) {
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId);
    }

    @CrossOrigin("*")
    @PutMapping("/api/modules/{moduleId}/lessons/{lessonId}")
    public Lesson updateLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId, String name, String description) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        Lesson lesson = module.getLessons().get(lessonId);
        lesson.setName(name);
        lesson.setDescription(description);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/modules/{moduleId}/lessons/{lessonId}")
    public Boolean deleteLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        module.getLessons().remove(lessonId);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId) == null;
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{moduleId}/lessons/{lessonId}/contents")
    public HashMap<Integer, Content> getContents(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId) {
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId).getContents();
    }

    @CrossOrigin("*")
    @PostMapping("/api/modules/{moduleId}/lessons/{lessonId}/contents")
    public Content postContent(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId, ContentType type, String url) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        Lesson lesson = module.getLessons().get(lessonId);
        Integer key = lesson.getContents().size();
        lesson.getContents().put(key, new Content(type, url));
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId).getContents().get(key);
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{moduleId}/lessons/{lessonId}/contents/{contentId}")
    public Content getContent(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId, @PathVariable("contentId") Integer contentId) {
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId).getContents().get(contentId);
    }

    @CrossOrigin("*")
    @PutMapping("/api/modules/{moduleId}/lessons/{lessonId}/contents/{contentId}")
    public Content updateContent(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId, @PathVariable("contentId") Integer contentId, ContentType type, String url) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        Lesson lesson = module.getLessons().get(lessonId);
        Content content = lesson.getContents().get(contentId);
        content.setType(type);
        content.setUrl(url);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId).getContents().get(contentId);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/modules/{moduleId}/lessons/{lessonId}/{contentId}")
    public Boolean deleteLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonId") Integer lessonId, @PathVariable("contentId") Integer contentId) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        module.getLessons().get(lessonId).getContents().remove(contentId);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonId).getContents().get(contentId) == null;
    }

}
