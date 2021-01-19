package pl.kniotes.elearn_course_viewer.Models.Module;

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
@Document("modules")
public class Module {
    @Id
    private String id;
    @NonNull
    @Indexed
    private String name;
    private String description;
    private HashMap<Integer, Lesson> lessons;
    private HashMap<Integer, String> downloadables; // links to files that can be downloaded, like notes, source code, etc.
}
