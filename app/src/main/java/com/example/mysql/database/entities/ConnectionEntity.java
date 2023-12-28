package com.example.mysql.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity(tableName = "connectionsTable", primaryKeys = {"ipAddress", "port", "user", "dbName"})
public class ConnectionEntity implements Serializable {

    @ColumnInfo(name = "ipAddress") @NonNull
    public String ipAddress;

    @ColumnInfo(name = "port") @NonNull
    public String port;

    @ColumnInfo(name = "user") @NonNull
    public String user;

    @ColumnInfo(name = "dbName") @NonNull
    public String dbName;

    @ColumnInfo(name = "currentTimeInMillies")
    public long currentTimeInMillies;

    public ConnectionEntity(String ipAddress, String port, String user, String dbName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.user = user;
        this.dbName = dbName;
        this.currentTimeInMillies = System.currentTimeMillis();

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getDbName() {
        return dbName;
    }

    public long getCurrentTimeInMillies() {
        return currentTimeInMillies;
    }

    public void setIpAddress(@NonNull String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(@NonNull String port) {
        this.port = port;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    public void setDbName(@NonNull String dbName) {
        this.dbName = dbName;
    }

    public void setCurrentTimeInMillies(long currentTimeInMillies) {
        this.currentTimeInMillies = currentTimeInMillies;
    }
}
