package org.dawiddc.egradebook.utils;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class DatastoreHandler {

    private static DatastoreHandler ds = new DatastoreHandler();
    private static Datastore datastore;

    private DatastoreHandler() {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("com.dawiddc.egradebook.model");
        datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "egradebook");
        datastore.ensureIndexes();
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}
