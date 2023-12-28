package com.example.mysql.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mysql.MyApplication;
import com.example.mysql.R;
import com.example.mysql.ViewModels.ConnectionsViewModel;
import com.example.mysql.ViewModels.viewModelFactories.ConnectionsViewModelFactory;
import com.example.mysql.adapters.ConnectionsRecyclerViewAdapter;
import com.example.mysql.database.entities.ConnectionEntity;
import com.example.mysql.databinding.ActivityMainBinding;
import com.example.mysql.ui.fragments.AddConnectionDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, NavigationView.OnNavigationItemSelectedListener {

    MyApplication applicationClass;
    ActivityMainBinding binding;
    Toolbar toolbar;
    DrawerLayout mDrawer;

    MainActivity context;
    ConnectionsViewModel viewModel;
    ConnectionsRecyclerViewAdapter recyclerViewAdapter;

    ArrayList<ConnectionEntity> allConnections;

    ConnectionEntity currentConnection;
    String currentConnectionPassword;

    ProgressDialog progressDialog;
    boolean connectionCanceled = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        recyclerViewAdapter.setOnItemClickListener(new ConnectionsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showConnectDialog(position);
            }

            @Override
            public void onDeleteClicked(int position, View view) {
                showPopup(view, position);
            }
        });

        viewModel.getAllConnections().observe(this, connectionEntities -> {
            allConnections.clear();
            allConnections = (ArrayList<ConnectionEntity>) connectionEntities;
            recyclerViewAdapter.updateUserList(connectionEntities);

        });
        binding.fab.setOnClickListener(view -> showAddConnectionDialog(null));

        viewModel.getNewConnectionData().observe(this, data -> {
            if(!data.isEmpty()){
                ConnectionEntity connection = new ConnectionEntity(data.get("ipAddress"), data.get("port"),data.get("user"), data.get("database"));
                viewModel.insetConnection(connection);
            }
        });

        viewModel.connect("", "", "", "", "", null)
                .observe(MainActivity.this, data -> {
                    if(data != null) {
                        progressDialog.dismiss();
                        if(!connectionCanceled) {
                        Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                        intent.putExtra("ipAddress", currentConnection.getIpAddress());
                        intent.putExtra("port", currentConnection.getPort());
                        intent.putExtra("database", currentConnection.getDbName());
                        intent.putExtra("user", currentConnection.getUser());
                        intent.putExtra("password", currentConnectionPassword);
                        intent.putStringArrayListExtra("databases", data.get("databases"));
                        intent.putStringArrayListExtra("tables", data.get("tables"));
                        startActivity(intent);
                        }
                    }
                });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.deleteAllConnectionsMenuItem:
                viewModel.deleteAllConnections();
                return true;
            case R.id.addConnectionMenuItem:
                showAddConnectionDialog(null);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this, R.style.MyDialogTheme);
        dialogBuilder.setMessage("Hi, my name is abdenour and i am a native android developer.\n" +
                "Contact : abdenour.bacha@yahoo.fr\n" +
                "https://github.com/Dz-abdou");
        dialogBuilder.setTitle("About us");
        dialogBuilder.show();
        return false;
    }



    public void showAddConnectionDialog(ConnectionEntity connectionEntity) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddConnectionDialogFragment newFragment;
        if(connectionEntity != null)
            newFragment = new AddConnectionDialogFragment(viewModel, connectionEntity);
        else
            newFragment = new AddConnectionDialogFragment(viewModel);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();



    }

    public void showConnectDialog(int position) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this, R.style.MyDialogTheme);
        final View customLayout = getLayoutInflater().inflate(R.layout.connect_dialog, null);
        dialogBuilder.setTitle(getString(R.string.connect));
        dialogBuilder.setNeutralButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        dialogBuilder.setPositiveButton(getString(R.string.done), (dialogInterface, i) -> {
            connectionCanceled = false;
            TextInputEditText et = customLayout.findViewById(R.id.connectPasswordEt);
            currentConnection = allConnections.get(position);
            Log.i("ipAddress", currentConnection.getIpAddress());
            Log.i("port", currentConnection.getPort());
            Log.i("database", currentConnection.getDbName());
            Log.i("user", currentConnection.getUser());
            currentConnectionPassword = et.getText().toString();
            progressDialog.show();
            viewModel.connect(currentConnection.getIpAddress(),
                    currentConnection.getPort(),
                    currentConnection.getDbName(),
                    currentConnection.getUser(),
                    currentConnectionPassword,
                    progressDialog
            );
        });
        dialogBuilder.setView(customLayout);
        dialogBuilder.show();
    }
    public void showPopup(View view, int position) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.connection_item_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.edit:{
                    ConnectionEntity connectionToEdit = allConnections.get(position);
                    showAddConnectionDialog(connectionToEdit);
                    return true;
                }
                case R.id.delete: {
                    ArrayList<ConnectionEntity> connectionToDelete = new ArrayList<>();
                    connectionToDelete.add(allConnections.get(position));
                    viewModel.deleteConnection(connectionToDelete);
                    return true;
                }
                default:
                    return false;
            }
        });
        popup.show();
    }

    public void init(){
        applicationClass = (MyApplication) getApplicationContext();
        progressDialog = new ProgressDialog(MainActivity.this, R.style.MyDialogTheme);
        progressDialog.setMessage(getString(R.string.checking_connection));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(dialogInterface -> connectionCanceled = true);
        binding.nvView.setNavigationItemSelectedListener(this);
        allConnections = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(toolbar);
        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        context = this;
        recyclerViewAdapter = new ConnectionsRecyclerViewAdapter();

        binding.connectionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = new ViewModelProvider(this, new ConnectionsViewModelFactory(this.getApplication(), applicationClass.getExecutorService(), binding.mainProgressBar, applicationClass.getMainThreadHandler())).get(ConnectionsViewModel.class);
        binding.connectionsRecyclerView.setAdapter(recyclerViewAdapter);
    }

}