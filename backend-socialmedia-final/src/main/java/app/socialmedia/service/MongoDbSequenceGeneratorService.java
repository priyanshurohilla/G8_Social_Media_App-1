package app.socialmedia.service;

import app.socialmedia.model.DbSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class MongoDbSequenceGeneratorService {

 @Autowired
    private MongoOperations mongoOperations;

 public int getNextSequence(String sequenceName){
     DbSequence counter =mongoOperations.findAndModify(Query.query(where("_sequenceName").is(sequenceName)),
             new Update().inc("sequence_number",1), options().returnNew(true).upsert(true),
             DbSequence.class);
     return  counter.getSequence_number();
 }
    }



