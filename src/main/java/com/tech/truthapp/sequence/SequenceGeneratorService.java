package com.tech.truthapp.sequence;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tech.truthapp.model.DBSequence;

@Service
public class SequenceGeneratorService {

	@Autowired
	private MongoOperations mongoOperations;

	/**
	 * 
	 * @param sequenceName
	 * @return
	 */
    public Integer generateUserSequence(String sequenceName) {
    	//get sequence no
        Query query = new Query(Criteria.where("_id").is(sequenceName));
        //update the sequence no
        Update update = new Update().inc("sequence", 1);
        //modify in document
        DBSequence counter = mongoOperations
                .findAndModify(query,
                        update, options().returnNew(true).upsert(true),
                        DBSequence.class);
        return !Objects.isNull(counter) ? counter.getSequence() : 1;
    }
}
