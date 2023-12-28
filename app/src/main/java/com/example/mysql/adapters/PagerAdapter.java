package com.example.mysql.adapters;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mysql.database.entities.ConnectionEntity;
import com.example.mysql.ui.activities.DatabaseActivity;
import com.example.mysql.ui.fragments.MysqlDatabaseFragment;
import com.example.mysql.ui.fragments.TableRecordsFragment;
import com.example.mysql.ui.fragments.TablesFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PagerAdapter extends FragmentStateAdapter {

    private int numberOfTabs;
    private ConnectionEntity currentConnection;
    private String password;
    private ArrayList<String> databases;
    private ArrayList<String> selectedDatabaseTables;


    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int numberOfTabs,
                        ConnectionEntity currentConnection, String password, ArrayList<String> databases, ArrayList<String> tables) {
        super(fragmentManager, lifecycle);
        this.numberOfTabs = numberOfTabs;
        this.currentConnection = currentConnection;
        this.password = password;
        this.databases = databases;
        this.selectedDatabaseTables = tables;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("ipAddress", currentConnection.getIpAddress());
            bundle.putString("database", currentConnection.getDbName());
            bundle.putString("port", currentConnection.getPort());
            bundle.putString("user", currentConnection.getUser());
            bundle.putString("password", password);
            bundle.putStringArrayList("databases", databases);
            bundle.putStringArrayList("selectedDatabaseTables", selectedDatabaseTables);

            Fragment fragment = new MysqlDatabaseFragment();
                fragment.setArguments(bundle);
                return fragment;
        }
        else if(position == 1) {
            Bundle bundle = new Bundle();
            bundle.putString("ipAddress", currentConnection.getIpAddress());
            bundle.putString("database", currentConnection.getDbName());
            bundle.putString("port", currentConnection.getPort());
            bundle.putString("user", currentConnection.getUser());
            bundle.putString("password", password);
            Fragment fragment = new TablesFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
        else if(position == 2){
            Bundle bundle = new Bundle();
            bundle.putString("ipAddress", currentConnection.getIpAddress());
            bundle.putString("database", currentConnection.getDbName());
            bundle.putString("port", currentConnection.getPort());
            bundle.putString("user", currentConnection.getUser());
            bundle.putString("password", password);
            Fragment fragment = new TableRecordsFragment();
            fragment.setArguments(bundle);

            return fragment;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return numberOfTabs;
    }


}
