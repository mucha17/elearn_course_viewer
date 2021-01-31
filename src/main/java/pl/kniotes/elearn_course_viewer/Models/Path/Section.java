package pl.kniotes.elearn_course_viewer.Models.Path;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Section {
    @NonNull
    private String name;
    private String description;
    private List<OrderedCourse> courses;
}
