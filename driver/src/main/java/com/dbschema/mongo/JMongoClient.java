package com.dbschema.mongo;


import com.mongodb.ServerAddress;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;


public class JMongoClient {

    private final static Map<String,MongoClient> mongoClients = new LinkedHashMap<>();
    private final static Object mongoClientsSync = new Object();
    private MongoClient mongoClient ;
    public final String databaseName;

    public  JMongoClient( String uri ){
        // TODO HERE CAN FIND DATABASE FROM URI
        MongoClientOptions.Builder options = MongoClientOptions.builder();
        options.socketKeepAlive(true);
        final MongoClientURI clientURI = new MongoClientURI(uri,options );
        this.databaseName = clientURI.getDatabase();
        if (!mongoClients.containsKey(uri)) {
            synchronized(mongoClientsSync) {
                if (!mongoClients.containsKey(uri)) {
                    MongoClient client =new MongoClient(clientURI);
                    mongoClients.put(uri, client);


                }
            }
        }
        this.mongoClient = mongoClients.get(uri);
    }

    public MongoClientOptions getMongoClientOptions() {
        return mongoClient.getMongoClientOptions();
    }

    public List<MongoCredential> getCredentialsList() {
        return mongoClient.getCredentialsList();
    }

    public MongoIterable<String> listDatabaseNames() {
        return mongoClient.listDatabaseNames();
    }

    public ListDatabasesIterable<Document> listDatabases() {
        return mongoClient.listDatabases();
    }

    public <T> ListDatabasesIterable<T> listDatabases(Class<T> clazz) {
        return mongoClient.listDatabases(clazz);
    }

    public JMongoDatabase getDatabase(String databaseName) {
        return new JMongoDatabase( mongoClient.getDatabase(databaseName));
    }

    public void testConnectivity(){
        mongoClient.getAddress();
    }
    public MongoClient getClient(){
        return  mongoClient;
    }
}
