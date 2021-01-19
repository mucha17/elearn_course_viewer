package pl.kniotes.elearn_course_viewer.Models.Module;

import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Content {
    @NonNull
    private ContentType type;
    private String url;
}
