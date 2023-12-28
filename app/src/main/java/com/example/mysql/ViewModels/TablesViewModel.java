package com.example.mysql.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.widget.ViewPager2;
import com.example.mysql.repositories.MysqlDatabaseRepository;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.concurrent.ExecutorService;

public class TablesViewModel extends AndroidViewModel {

    private final MysqlDatabaseRepository mysqlDatabaseRepository;
    private final MutableLiveData<String> selectedDatabase;
    private final MutableLiveData<String> selectedTable;
    private final String ipAddress;
    private final String port;
    private final String user;
    private final String password;



    public void setSelectedDatabase(String selectedDatabase) {
        this.selectedDatabase.setValue(selectedDatabase);
    }

    public MutableLiveData<String> getSelectedDatabase(){
        return selectedDatabase;
    }

    public TablesViewModel(@NonNull Application application, ExecutorService executorService, Handler handler,
                           String ipAddress, String port, String user, String password) {
        super(application);
        mysqlDatabaseRepository = new MysqlDatabaseRepository(application, executorService, handler);
        this.ipAddress = ipAddress;
        this.port = port;
        this.user = user;
        this.password = password;
        this.selectedDatabase = new MutableLiveData<>();
        this.selectedTable = new MutableLiveData<>();
    }

    public void setSelectedDatabaseTables(String database, ProgressBar progressBar, ViewPager2 viewPager) {

        mysqlDatabaseRepository.setSelectedDatabaseTables(ipAddress, port, user, database, password, progressBar, viewPager);
        Log.i("myTag", ipAddress+ port +user +database+ password);
    }

    public MutableLiveData<ArrayList<String>> getSelectedDatabaseTables() {
        return mysqlDatabaseRepository.getSelectedDatabaseTables();
    }

    public void setSelectedTableRecordsAndColumns(String database,String tableName, ProgressBar progressBar,ViewPager2 viewPager) {
        mysqlDatabaseRepository.setSelectedTableRecordsAndColumns(ipAddress, port, user, database, password, tableName, progressBar, viewPager);
    }

    public MutableLiveData<HashMap<String, Object>> getSelectedTableData(){
        return mysqlDatabaseRepository.getSelectedTableData();
    }


    public void setSelectedTable(String table) {
        this.selectedTable.setValue(table);
    }

    public MutableLiveData<String> getSelectedTable() {
        return selectedTable;
    }
}

