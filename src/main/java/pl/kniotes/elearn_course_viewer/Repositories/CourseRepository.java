package pl.kniotes.elearn_course_viewer.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.kniotes.elearn_course_viewer.Models.Course.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    Course findCourseById(String id);

    Course deleteCourseById(String id);
}
