package pl.kniotes.elearn_course_viewer.Models.Path;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.kniotes.elearn_course_viewer.Models.Course.Course;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderedCourse {
    @NonNull
    private int orderIndex;
    @DBRef
    private Course course;
}
