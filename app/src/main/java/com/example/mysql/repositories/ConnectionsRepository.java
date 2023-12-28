package com.example.mysql.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mysql.database.ConnectionsDatabase;
import com.example.mysql.database.daos.ConnectionDao;
import com.example.mysql.database.entities.ConnectionEntity;

import java.util.List;

public class ConnectionsRepository {

    private ConnectionDao connectionDao;
    private LiveData<List<ConnectionEntity>> allConnections;

    public ConnectionsRepository(Application application) {
        this.connectionDao = ConnectionsDatabase.getDatabase(application).connectionDao();
        allConnections = this.connectionDao.getAllConnections();
    }

    public LiveData<List<ConnectionEntity>> getAllConnections() {
        return allConnections;
    }

    public void insertConnection(ConnectionEntity connection) {
        ConnectionsDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                connectionDao.insert(connection);
            }
        });
    }

    public void deleteConnections(List<ConnectionEntity> connections){
        ConnectionsDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                connectionDao.delete(connections);
            }
        });
    }

    public void deleteAllConnections(){
        ConnectionsDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                connectionDao.deleteAllConnections();
            }
        });
    }

    public void updateConnection(ConnectionEntity connection){
        ConnectionsDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                connectionDao.update(connection);
            }
        });
    }
}
