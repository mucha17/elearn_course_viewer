package pl.kniotes.elearn_course_viewer.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
        if (name != null && !name.isBlank()) {
            module.setName(name);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not set");
        }
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
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        if (name != null && !name.isBlank()) {
            module.setName(name);
        } else {
            if (module.getName() == null || module.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module name seems to be empty. Set the name in order to update");
            }
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
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        return module.getLessons();
    }

    @CrossOrigin("*")
    @PostMapping("/api/modules/{moduleId}/lessons")
    public Lesson postLesson(@PathVariable("moduleId") String moduleId, String name, String description) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        Lesson lesson = new Lesson();
        if (name != null && !name.isBlank()) {
            lesson.setName(name);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not set");
        }
        if (description != null && !description.isBlank()) {
            lesson.setDescription(description);
        }
        Integer key = module.getLessons().size();
        module.getLessons().put(key, lesson);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(key);
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{moduleId}/lessons/{lessonKey}")
    public Lesson getLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        return module.getLessons().get(lessonKey);
    }

    @CrossOrigin("*")
    @PutMapping("/api/modules/{moduleId}/lessons/{lessonKey}")
    public Lesson updateLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey, String name, String description) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        Lesson lesson = module.getLessons().get(lessonKey);
        if (lesson == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is invalid");
        }
        if (name != null && !name.isBlank()) {
            lesson.setName(name);
        } else {
            if (lesson.getName() == null || lesson.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course name seems to be empty. Set the name in order to update");
            }
        }
        lesson.setDescription(description);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonKey);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/modules/{moduleId}/lessons/{lessonKey}")
    public Boolean deleteLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        if (module.getLessons().get(lessonKey) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is invalid");
        }
        module.getLessons().remove(lessonKey);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonKey) == null;
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{moduleId}/lessons/{lessonKey}/contents")
    public HashMap<Integer, Content> getContents(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        Lesson lesson = module.getLessons().get(lessonKey);
        if (lesson == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is invalid");
        }
        return lesson.getContents();
    }

    @CrossOrigin("*")
    @PostMapping("/api/modules/{moduleId}/lessons/{lessonKey}/contents")
    public Content postContent(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey, ContentType type, String url) {
        if (type == null || !(type.equals(ContentType.ARTICLE) || type.equals(ContentType.PODCAST) || type.equals(ContentType.VIDEO))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is not set or is not applicable");
        }
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        if (module.getLessons().get(lessonKey) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is invalid");
        }
        Lesson lesson = module.getLessons().get(lessonKey);
        if (lesson == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is not set.");
        }
        Integer key = lesson.getContents().size();
        lesson.getContents().put(key, new Content(type, url));
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonKey).getContents().get(key);
    }

    @CrossOrigin("*")
    @GetMapping("/api/modules/{moduleId}/lessons/{lessonKey}/contents/{contentId}")
    public Content getContent(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey, @PathVariable("contentId") Integer contentId) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        Lesson lesson = module.getLessons().get(lessonKey);
        if (lesson == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is not set.");
        }
        return lesson.getContents().get(contentId);
    }

    @CrossOrigin("*")
    @PutMapping("/api/modules/{moduleId}/lessons/{lessonKey}/contents/{contentId}")
    public Content updateContent(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey, @PathVariable("contentId") Integer contentId, ContentType type, String url) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        if (module.getLessons().get(lessonKey) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is invalid");
        }
        Lesson lesson = module.getLessons().get(lessonKey);
        Content content = lesson.getContents().get(contentId);
        if (type != null && (type.equals(ContentType.ARTICLE) || type.equals(ContentType.PODCAST) || type.equals(ContentType.VIDEO))) {
            content.setType(type);
        } else {
            if (content.getType() == null || !(content.getType().equals(ContentType.ARTICLE) || content.getType().equals(ContentType.PODCAST) || content.getType().equals(ContentType.VIDEO))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson content type seems to be not set or is not applicable. Set the content type in order to update.");
            }
        }

        content.setUrl(url);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonKey).getContents().get(contentId);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/modules/{moduleId}/lessons/{lessonKey}/{contentId}")
    public Boolean deleteLesson(@PathVariable("moduleId") String moduleId, @PathVariable("lessonKey") Integer lessonKey, @PathVariable("contentId") Integer contentId) {
        Module module = this.moduleRepository.findModuleById(moduleId);
        if (module == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module is invalid");
        }
        if (module.getLessons().get(lessonKey) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lesson is invalid");
        }
        module.getLessons().get(lessonKey).getContents().remove(contentId);
        this.moduleRepository.save(module);
        return this.moduleRepository.findModuleById(moduleId).getLessons().get(lessonKey).getContents().get(contentId) == null;
    }

}
