package com.wtb.javatool.mongoDao.impl;

import com.fy.wetoband.tool.Tool;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.wtb.javatool.mongoDao.docDao;
import org.bson.Document;

public class docDaoImpl extends Tool implements docDao {
    public void updateDoc(){
        MongoClient mongoClient = this.getMongoClient();
        MongoDatabase db = mongoClient.getDatabase("doc_check");
        MongoCollection<Document> collection = db.getCollection("extraction");
        mongoClient.close();
    }
}
