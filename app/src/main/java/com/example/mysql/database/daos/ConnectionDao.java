package com.example.mysql.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mysql.database.entities.ConnectionEntity;

import java.util.List;

@Dao
public interface ConnectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(ConnectionEntity connectionEntity);

    @Update
    public void update(ConnectionEntity connectionEntity);

    @Delete
    public void delete(List<ConnectionEntity> connectionEntities);

    @Query("SELECT * FROM connectionsTable ORDER BY currentTimeInMillies DESC")
    public LiveData<List<ConnectionEntity>> getAllConnections();

    @Query("DELETE FROM connectionsTable")
    public void deleteAllConnections();

}
