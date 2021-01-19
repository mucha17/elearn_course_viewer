package pl.kniotes.elearn_course_viewer.Models.Module;

import lombok.*;

import java.util.HashMap;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Lesson {
    @NonNull
    private String name;
    private String description;
    private HashMap<Integer, Content> contents; // lesson contents like video, reading, podcast, etc.
}
