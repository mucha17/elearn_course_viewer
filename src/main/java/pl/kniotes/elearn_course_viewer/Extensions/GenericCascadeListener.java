package pl.kniotes.elearn_course_viewer.Extensions;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class GenericCascadeListener extends AbstractMongoEventListener<Object> {
    final private MongoTemplate mongoTemplate;

    public GenericCascadeListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent event) {
        Object document = event.getSource();
        ReflectionUtils.doWithFields(document.getClass(), docField -> {
            ReflectionUtils.makeAccessible(docField);

            if (docField.isAnnotationPresent(DBRef.class) && !docField.isAnnotationPresent(SkipCascadeSave.class)) {
                final Object fieldValue = docField.get(document);

                // First save sub-documents that are referenced
                this.mongoTemplate.save(fieldValue);
            }
        });
    }
}
