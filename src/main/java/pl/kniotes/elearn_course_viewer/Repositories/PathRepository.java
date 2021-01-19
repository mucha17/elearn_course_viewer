package pl.kniotes.elearn_course_viewer.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.kniotes.elearn_course_viewer.Models.Path.Path;

@Repository
public interface PathRepository extends MongoRepository<Path, String> {
    Path findPathById(String id);
}
