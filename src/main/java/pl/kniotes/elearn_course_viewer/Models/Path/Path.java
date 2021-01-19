package pl.kniotes.elearn_course_viewer.Models.Path;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("paths")
public class Path {
    @Id
    private String id;
    @NonNull
    @Indexed
    private String name;
    private String description;
    private HashMap<Integer, Section> sections;
    private String exam; // exam's url
}
