//package ru.web_marks.view;
//
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import ru.web_marks.model.MongoModels;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MongoDBPOperations {
//
//    public void saveInstance(MongoOperations mongoOperation, MongoModels instance) {
//        mongoOperation.save(instance);
//        System.out.println("Instance saved successfully");
//        // instance object got created with id.
//        System.out.println("Instance : " + instance);
//    }
//
//    public void searchInstance(MongoOperations mongoOperation, String critera, String value) {
//        // query to search instance
//        Query searchInstance = new Query(Criteria.where(critera).is(value));
//
//        // find instance based on the query
//        MongoModels resultInstance = mongoOperation.findOne(searchInstance, MongoModels.class);
//        System.out.println("Instance found!!");
//        System.out.println("Instance details: " + resultInstance);
//    }
//
//    public void updateInstance(MongoOperations mongoOperation, String critera, String value, String updateCriteria, String updateValue) {
//        // query to search instance
//        Query searchInstance = new Query(Criteria.where(critera).is(value));
//        mongoOperation.updateFirst(searchInstance, Update.update(updateCriteria, updateValue),
//                MongoModels.class);
//        System.out.println("Instance got updated successfully");
//    }
//
//    public void updateInstance(MongoOperations mongoOperation, String critera, String value, String updateCriteria, ArrayList<?> updateValue) {
//        // query to search instance
//        Query searchInstance = new Query(Criteria.where(critera).is(value));
//        mongoOperation.updateFirst(searchInstance, Update.update(updateCriteria, updateValue),
//                MongoModels.class);
//        System.out.println("Instance got updated successfully");
//    }
//
//    public void getAllInstances(MongoOperations mongoOperation) {
//        List listInstance = mongoOperation.findAll(MongoModels.class);
//        for (Object instance : listInstance) {
//            System.out.println("Instance = " + instance);
//        }
//    }
//
//    public void removeInstances(MongoOperations mongoOperation, String critera, String value) {
//        Query searchInstance = new Query(Criteria.where(critera).is(value));
//        mongoOperation.remove(searchInstance, MongoModels.class);
//        System.out.println("Instance removed successfully!! ");
//    }
//
//    public void updateInstance(MongoOperations mongoOperation, String critera, String value, String updateCriteria, int updateValue) {
//        // query to search instance
//        Query searchInstance = new Query(Criteria.where(critera).is(value));
//        mongoOperation.updateFirst(searchInstance, new Update().push(updateCriteria, updateValue),
//                MongoModels.class);
//        System.out.println("Instance got updated successfully");
//    }
//
//}
//
