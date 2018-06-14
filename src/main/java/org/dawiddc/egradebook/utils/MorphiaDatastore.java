package org.dawiddc.egradebook.utils;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorphiaDatastore {

    private static Datastore datastore;

    static {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("com.dawiddc.egradebook.model");
        datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "egradebook");
        datastore.ensureIndexes();
    }

    private MorphiaDatastore() {
        throw new IllegalStateException("Utility class");
    }

    public static Datastore getDatastore() {
        if (datastore == null) {
            new MorphiaDatastore();
        }
        return datastore;
    }
}
