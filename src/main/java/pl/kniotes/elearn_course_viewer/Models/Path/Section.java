package pl.kniotes.elearn_course_viewer.Models.Path;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.kniotes.elearn_course_viewer.Models.Course.Course;

import java.util.HashMap;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Section {
    @NonNull
    private String name;
    private String description;
    @DBRef
    private HashMap<Integer, Course> courses;
    private String exam; // exam's url
}
