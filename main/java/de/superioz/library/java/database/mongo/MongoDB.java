package de.superioz.library.java.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.Collections;

/**
 * This class was created as a part of SuperLibrary (Spigot)
 *
 * @author Superioz
 */
public class MongoDB {

    private MongoClient client;
    private MongoDatabase database;

    private ServerAddress serverAddress;
    private MongoCredential authentification;

    public static final int DEFAULT_PORT = 27017;
    public static final int DEFAULT_SHARD_PORT = 27018;
    public static final int DEFAULT_CONFIG_PORT = 27019;
    public static final int DEFAULT_WEBSTATUS_PORT = 28017;

    // ========================================= Constructor =======================================

    public MongoDB(String serverAddress, int port){
        this.serverAddress = new ServerAddress(serverAddress, port);
        this.client = new MongoClient(this.serverAddress);
    }

    public MongoDB(String serverAddress, int port, String username, String database, String password){
        this.serverAddress = new ServerAddress(serverAddress, port);
        this.authentification = MongoCredential.createCredential(username, database, password.toCharArray());

        this.client = new MongoClient(this.serverAddress, Collections.singletonList(this.authentification));
        this.connect(database);
    }


    // ========================================= OTHER =======================================

    public void connect(String database){
        this.database = this.client.getDatabase(database);
    }

    public MongoDatabase getDatabase(){
        return this.database;
    }

    public void createCollection(String coll){
        getDatabase().createCollection(coll);
    }

    public void deleteCollection(String coll){
        if(collectionExists(coll))
            getDatabase().getCollection(coll).drop();
    }

    public boolean collectionExists(String name){
        for (final String n : getDatabase().listCollectionNames()) {
            if (n.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public MongoCollection getCollection(String collection){
        if(!collectionExists(collection)){
            createCollection(collection);
        }
        return getDatabase().getCollection(collection);
    }

    public String getDatabaseName(){
        return getDatabase().getName();
    }

    public String getServerAddress(){
        return serverAddress.getHost();
    }

    public int getServerPort(){
        return serverAddress.getPort();
    }

    public MongoCredential getAuthentification(){
        return authentification;
    }

}
