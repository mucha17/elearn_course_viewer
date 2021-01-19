package pl.kniotes.elearn_course_viewer.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.kniotes.elearn_course_viewer.Models.Module.Module;

@Repository
public interface ModuleRepository extends MongoRepository<Module, String> {
    Module findModuleById(String id);
}
