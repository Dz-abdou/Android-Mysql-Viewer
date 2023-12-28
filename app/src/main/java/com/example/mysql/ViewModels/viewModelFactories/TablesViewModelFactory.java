package com.example.mysql.ViewModels.viewModelFactories;


import android.app.Application;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mysql.ViewModels.ConnectionsViewModel;
import com.example.mysql.ViewModels.TablesViewModel;

import java.util.concurrent.ExecutorService;

public class TablesViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private ExecutorService executorService;
    private Handler handler;
    private String ipAddress;
    private String port;
    private String user;
    private String password;

    public TablesViewModelFactory(Application application,  ExecutorService executorService, Handler handler,
                                  String ipAddress, String port, String user, String password) {
        mApplication = application;
        this.executorService = executorService;
        this.handler = handler;
        this.ipAddress = ipAddress;
        this.port = port;
        this.user = user;
        this.password = password;

    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new TablesViewModel(mApplication, executorService, handler, ipAddress, port, user, password);
    }
}
