package com.example.mysql.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.example.mysql.R;
import com.example.mysql.ViewModels.ConnectionsViewModel;
import com.example.mysql.database.entities.ConnectionEntity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class AddConnectionDialogFragment extends DialogFragment {

    private final ConnectionsViewModel viewModel;

    private TextInputEditText ipAddressEt;
    private TextInputEditText portEt;
    private  TextInputEditText databaseEt;
    private TextInputEditText userEt;
    private  TextInputEditText passwordEt;

    private  TextInputLayout ipAddressIl;
    private  TextInputLayout portIl;
    private  TextInputLayout databaseIl;
    private  TextInputLayout userIl;

    private  MaterialToolbar toolbar;
    private  View view;


    ConnectionEntity connectionToUpdate;

    public AddConnectionDialogFragment(ConnectionsViewModel viewModel) {
        this.viewModel = viewModel;

    }

    public AddConnectionDialogFragment(ConnectionsViewModel viewModel, ConnectionEntity connectionToUpdate) {
        this.viewModel = viewModel;
        this.connectionToUpdate = connectionToUpdate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_connection_dialog_layout, container, false);
        init();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> popBackToActivity(((AppCompatActivity) getActivity()).getSupportFragmentManager()));
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void popBackToActivity(FragmentManager manager) {
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    void init(){
        ipAddressEt = view.findViewById(R.id.ipAddressTf);
        portEt = view.findViewById(R.id.portTF);
        databaseEt = view.findViewById(R.id.databaseTF);
        userEt = view.findViewById(R.id.userTF);
        passwordEt = view.findViewById(R.id.passwordTF);

        ipAddressIl = view.findViewById(R.id.ipAddressTl);
        portIl = view.findViewById(R.id.portTl);
        databaseIl = view.findViewById(R.id.databaseTl);
        userIl = view.findViewById(R.id.userTl);
        toolbar = view.findViewById(R.id.addConnectionToolbar);
        setHasOptionsMenu(true);

        if(connectionToUpdate != null) {
            ipAddressEt.setText(connectionToUpdate.getIpAddress());
            portEt.setText(connectionToUpdate.getPort());
            databaseEt.setText(connectionToUpdate.getDbName());
            userEt.setText(connectionToUpdate.getUser());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_connection_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(){

        String ipAddress = ipAddressEt.getText().toString();
        String port = portEt.getText().toString();
        String database = databaseEt.getText().toString();
        String user = userEt.getText().toString();
        String password = passwordEt.getText().toString();

        if(ipAddress.isEmpty()) {
            ipAddressIl.setError(getString(R.string.this_field_cannot_be_empty));
            return;
        }
        ipAddressIl.setError(null);
        if(port.isEmpty()) {
            portIl.setError(getString(R.string.this_field_cannot_be_empty));
            return;
        }
        portIl.setError(null);
        if(database.isEmpty()) {
            databaseIl.setError(getString(R.string.this_field_cannot_be_empty));
            return;
        }
        databaseIl.setError(null);
        if(user.isEmpty()) {
            userIl.setError(getString(R.string.this_field_cannot_be_empty));
            return;
        }
        userIl.setError(null);

        HashMap<String, String> data = new HashMap<>();
        data.put("ipAddress", ipAddress);
        data.put("port", port);
        data.put("database", database);
        data.put("user", user);
        data.put("password", password);

        if(connectionToUpdate != null) {
            ArrayList<ConnectionEntity> arrayList = new ArrayList<>();
            arrayList.add(connectionToUpdate);
            viewModel.deleteConnection(arrayList);
        }
        viewModel.createNewConnection(data);
        popBackToActivity(((AppCompatActivity) getActivity()).getSupportFragmentManager());
    }

}
