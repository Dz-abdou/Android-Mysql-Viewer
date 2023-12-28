package com.example.mysql.repositories;

import android.app.Application;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mysql.MysqlHelperClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;


public class MysqlDatabaseRepository {


    Application application;
    MutableLiveData<ArrayList<String>> databases;
    MutableLiveData<ArrayList<String>> selectedDatabaseTables;
    MutableLiveData<HashMap<String, Object>> selectedTableData;
    MutableLiveData<String> selectedTableName;
    ExecutorService executorService;
    Handler handler;
    MysqlHelperClass mysqlHelperClass;





    public MysqlDatabaseRepository(Application application,ExecutorService executorService, Handler handler) {
        this.application = application;
        this.databases = new MutableLiveData<>();
        this.selectedDatabaseTables = new MutableLiveData<>();
        this.selectedTableData = new MutableLiveData<>();
        this.selectedTableName = new MutableLiveData<>();
        this.executorService = executorService;
        mysqlHelperClass = new MysqlHelperClass();
        this.handler = handler;
    }

    public void setSelectedTableRecordsAndColumns(String ipAddress, String port, String user, String database,
                                                  String password, String tableName, ProgressBar progressBar,ViewPager2 viewPager) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> data = new HashMap<>();
                Exception exception = null;
                try {

                    data = mysqlHelperClass.getTableRecords(ipAddress.trim(), port.trim(), database.trim(), user.trim(), password.trim(), tableName);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    exception = throwables;
                }
                selectedTableData.postValue(data);
                Exception finalException = exception;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if(finalException == null) {
                            viewPager.setCurrentItem(2);
                        } else {
                            Toast.makeText(application, finalException.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public MutableLiveData<ArrayList<String>> getSelectedDatabaseTables() {
        return selectedDatabaseTables;
    }
    public void setSelectedDatabaseTables(String ipAddress, String port,String user, String database, String password, ProgressBar progressBar, ViewPager2 viewPager){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> data = new ArrayList<>();
                Exception exception = null;
                try {
                    data = mysqlHelperClass.getDatabaseTables(ipAddress, port, database, user, password);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    exception = throwables;
                }

                Exception finalException = exception;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(finalException != null)
                            Toast.makeText(application, finalException.getMessage(), Toast.LENGTH_SHORT).show();

                        if(progressBar != null)
                            progressBar.setVisibility(View.GONE);

                        if(viewPager != null && finalException == null)
                            viewPager.setCurrentItem(1);
                    }
                });
                selectedDatabaseTables.postValue(data);

            }
        });
    }

    public MutableLiveData<HashMap<String, Object>> getSelectedTableData() {

        return selectedTableData;
    }

}
