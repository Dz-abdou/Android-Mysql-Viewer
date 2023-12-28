package com.example.mysql.ViewModels.viewModelFactories;


import android.app.Application;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mysql.ViewModels.ConnectionsViewModel;

import java.util.concurrent.ExecutorService;

public class ConnectionsViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private ExecutorService executorService;
    private ProgressBar progressBar;
    private Handler handler;


    public ConnectionsViewModelFactory(Application application,  ExecutorService executorService,
                                       ProgressBar progressBar, Handler handler) {
        mApplication = application;
        this.executorService = executorService;
        this.progressBar = progressBar;
        this.handler = handler;

    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ConnectionsViewModel(mApplication, executorService, progressBar, handler);
    }
}
