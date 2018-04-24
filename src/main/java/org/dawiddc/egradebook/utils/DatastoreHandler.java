package org.dawiddc.egradebook.utils;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class DatastoreHandler {
    private static DatastoreHandler Instance = new DatastoreHandler();
    private Datastore datastore;

    private DatastoreHandler() {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("com.dawiddc.egradebook.model");
        datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "egradebook");
        datastore.ensureIndexes();
    }

    public static DatastoreHandler getInstance() {
        return Instance;
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
