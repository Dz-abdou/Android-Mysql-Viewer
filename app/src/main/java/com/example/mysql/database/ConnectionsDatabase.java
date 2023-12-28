package com.example.mysql.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mysql.database.daos.ConnectionDao;
import com.example.mysql.database.entities.ConnectionEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ConnectionEntity.class}, version = 1, exportSchema = false)
public abstract class ConnectionsDatabase extends RoomDatabase {

    public abstract ConnectionDao connectionDao();

    public static volatile ConnectionsDatabase INSTANCE;
//    public static final int NUMBER_OF_THREADS = 4;
//    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ConnectionsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ConnectionsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ConnectionsDatabase.class, "connectionsDb")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
