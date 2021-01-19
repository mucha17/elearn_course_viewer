package pl.kniotes.elearn_course_viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import pl.kniotes.elearn_course_viewer.Extensions.GenericCascadeListener;

@TestConfiguration
public class DatabaseTestConfiguration {

    @Autowired
    private MappingMongoConverter mongoConverter;

    @Bean
    GenericCascadeListener genericCascadeListener(MongoTemplate mongoTemplate) {
        return new GenericCascadeListener(mongoTemplate);
    }

    @Bean("test-factory")
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/db-tests");
    }

    @Bean
    MongoTemplate mongoTemplate(@Qualifier("test-factory") MongoDatabaseFactory factory) {
        return new MongoTemplate(factory);
    }

    @Bean
    GridFsTemplate gridFsTemplate(@Qualifier("test-factory") MongoDatabaseFactory factory) {
        return new GridFsTemplate(factory, mongoConverter);
    }
}
