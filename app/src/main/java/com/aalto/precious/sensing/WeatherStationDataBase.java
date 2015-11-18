package com.aalto.precious.sensing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fida on 5.11.2015.
 */
public class WeatherStationDataBase extends SQLiteOpenHelper {

    /**
     * CRUD operations (create "add", read "get", update, delete) node + get all nodes + delete all nodes
     */

    // Nodes table name
    private static final String TABLE_NODES = "nodes";
    // Nodes Table Columns names
    //private static final String KEY_ID = "id";
    private static final String KEY_LOCATION = "location";
    //---------------------------------------------------------------------
    private static final String KEY_URI = "uri";
    //private static final String[] COLUMNS = {KEY_ID, KEY_LOCATION, KEY_URI};
    //private static final String[] COLUMNS = {KEY_LOCATION, KEY_URI};

    public WeatherStationDataBase(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create nodes table
        String CREATE_NODES_TABLE = "CREATE TABLE nodes ( " +
                "location TEXT, " +
                "uri TEXT )";

        // create nodes table
        db.execSQL(CREATE_NODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older nodes table if existed
        db.execSQL("DROP TABLE IF EXISTS nodes");

        // create fresh nodes table
        this.onCreate(db);
    }

    public void updateTable(ArrayList<WeatherStation> weatherStationList) {
        deleteTable();

        for (WeatherStation station : weatherStationList) {
            if (station != null) {
                addNode(station);
            }
        }
    }

    public void addNode(WeatherStation station) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, PreciousServerNode.getId());
        values.put(KEY_LOCATION, station.getLocation()); // get location
        values.put(KEY_URI, station.getUri()); // get uri
        // 3. insert
        db.insert(TABLE_NODES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
    }

    public WeatherStation getRelevantNode() {
    /*ToDo: Node Selection logic in case of multiple nodes
     * Currently Returns only the first online node*/
        List<WeatherStation> onlineServerList = getAllStations();
        if (onlineServerList.size() == 0)
            return null;
        else
            return onlineServerList.get(0);
    }

    // Get All Nodes
    public ArrayList<WeatherStation> getAllStations() {
        ArrayList<WeatherStation> weatherStations = new ArrayList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NODES;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build PreciousServerNode and add it to list
        WeatherStation newNode;
        if (cursor.moveToFirst()) {
            do {
                newNode = new WeatherStation();
                //PreciousServerNode.setId(Integer.parseInt(cursor.getString(0)));
                newNode.setLocation(cursor.getString(0));
                newNode.setUri(cursor.getString(1));
                // Add PreciousServerNode to preciousServerNodes
                weatherStations.add(newNode);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return preciousServerNodes
        return weatherStations;
    }

    // Deleting single PreciousServerNode
    private void deleteTable() {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. delete
        db.delete(TABLE_NODES, null, null);
        // 3. close
        db.close();
    }


/*
    public PreciousServerNode getNode(String uri) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_NODES, // a. table
                        COLUMNS, // b. column names
                        "uri" + "=?", // c. selections
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        // 4. build PreciousServerNode object
        PreciousServerNode PreciousServerNode = new PreciousServerNode();
        PreciousServerNode.setFriendlyName(cursor.getString(0));
        PreciousServerNode.setURI(cursor.getString(1));
        Log.d("getNode(" + uri + ")", PreciousServerNode.toString());
        // 5. return PreciousServerNode
        return PreciousServerNode;
    }

    // Deleting single PreciousServerNode
    public void deleteNode(PreciousServerNode PreciousServerNode) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_NODES,
                KEY_URI + " = ?",
                new String[]{String.valueOf(PreciousServerNode.getURI())});
        // 3. close
        db.close();
        Log.d("deleteNode", PreciousServerNode.toString());

    }*/
}
