package pl.kniotes.elearn_course_viewer.Models.Course;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("courses")
public class Course {
    @Id
    private String id;
    @NonNull
    @Indexed
    private String name;
    private String description;
    private List<OrderedModule> modules;
    private String exam; // exam's url
    private String instructor; // instructor's UID
}
