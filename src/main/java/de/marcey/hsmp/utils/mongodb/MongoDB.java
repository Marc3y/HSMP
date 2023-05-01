package de.marcey.hsmp.utils.mongodb;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDB {

    private String connectionStr = "mongodb://4981134336_hsmp-plugin:c77Js%26%24H%26Pc8jpikS4pj@157.90.26.182:27017/?authMechanism=SCRAM-SHA-256&authSource=admin&uuidRepresentation=standard";
    private String database;
    private MongoClient client;

    public MongoDB(String database){
        this.database = database;
    }

    public void openConnection(){
        this.client = MongoClients.create(connectionStr);
    }

    public void closeConnection(){
        getClient().close();
    }

    public MongoCollection<Document> getCollection(String name) {
        return getClient().getDatabase(getDatabase()).getCollection(name);
    }

    public Document find(String collection, String path, Object value){
        Document find = new Document(path, value);
        return getClient().getDatabase(getDatabase()).getCollection(collection).find(find).first();
    }

    public void set(String collection, Document document){
        getCollection(collection).insertOne(document);
    }

    public void update(String collection, Document search, Bson updates){
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            getCollection(collection).updateOne(search, updates, options);
        } catch (MongoException e){
            e.printStackTrace();
        }
    }

    private String getDatabase() {
        return database;
    }

    public MongoClient getClient() {
        return client;
    }

}
