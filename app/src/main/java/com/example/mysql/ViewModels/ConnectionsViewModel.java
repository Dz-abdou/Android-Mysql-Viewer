package com.example.mysql.ViewModels;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mysql.MysqlHelperClass;
import com.example.mysql.database.entities.ConnectionEntity;
import com.example.mysql.repositories.ConnectionsRepository;

import java.nio.charset.MalformedInputException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ConnectionsViewModel extends AndroidViewModel {

    ConnectionsRepository connectionsRepository;
    MutableLiveData<HashMap<String, String>> newConnectionData;
    MutableLiveData<HashMap<String, ArrayList<String>>> isConnectionValid;
    ExecutorService executorService;
    Application application;
    MysqlHelperClass mysqlHelperClass;
    Handler handler;

    static boolean calledFirsTime = true;

    public ConnectionsViewModel(@NonNull Application application, ExecutorService executorService,
                                ProgressBar progressBar, Handler handler) {
        super(application);
        this.application = application;
        this.connectionsRepository = new ConnectionsRepository(application);
        this.newConnectionData = new MutableLiveData<>();
        this.isConnectionValid = new MutableLiveData<HashMap<String, ArrayList<String>>>();
        this.executorService = executorService;
        this.handler = handler;
        this.mysqlHelperClass = new MysqlHelperClass();
    }

    public void insetConnection(ConnectionEntity connection) {
        connectionsRepository.insertConnection(connection);
    }

    public LiveData<List<ConnectionEntity>> getAllConnections() {
        return connectionsRepository.getAllConnections();
    }

    public void deleteConnection(List<ConnectionEntity> connections) {
        connectionsRepository.deleteConnections(connections);
    }

    public void deleteAllConnections() {
        connectionsRepository.deleteAllConnections();
    }

    public void updateConnection(ConnectionEntity connection){
        connectionsRepository.updateConnection(connection);
    }

    public MutableLiveData<HashMap<String, String>> getNewConnectionData(){
        return newConnectionData;
    }

    public void createNewConnection(HashMap<String, String> data) {
        newConnectionData.setValue(data);
    }

    public MutableLiveData<HashMap<String, ArrayList<String>>> connect(String ipAddress,String port,String database, String user, String password, ProgressDialog progressDialog){

        if(calledFirsTime){
            calledFirsTime = false;
            return this.isConnectionValid;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, ArrayList<String>> data = null;
                try {
                    data = mysqlHelperClass.connect(ipAddress, port, database, user, password);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(progressDialog.isShowing()) {
                                Toast.makeText(application, throwables.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
                isConnectionValid.postValue(data);
            }
        });
        return isConnectionValid;
    }


}
