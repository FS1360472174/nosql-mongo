package com.fs.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

/**
 * @author cnstonefang@gmail.com
 */
public class MongoApplication {
    public static void  main(String[] args) {
        try {
            MongoClient client = new MongoClient();
            DB db = client.getDB("fs");
            DBCollection postCollection = db.getCollection("post");
            DBCursor cursor = postCollection.find(new BasicDBObject("_id",new BasicDBObject("$gt",
                    2)));
            try {
                while(cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }
            client.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
