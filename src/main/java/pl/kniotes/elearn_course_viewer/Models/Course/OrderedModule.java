package pl.kniotes.elearn_course_viewer.Models.Course;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.kniotes.elearn_course_viewer.Models.Module.Module;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderedModule {
    @NonNull
    private int orderIndex;
    @DBRef
    private Module module;
}
