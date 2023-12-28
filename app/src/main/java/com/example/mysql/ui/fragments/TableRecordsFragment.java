package com.example.mysql.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mysql.MyApplication;
import com.example.mysql.ViewModels.TablesViewModel;
import com.example.mysql.ViewModels.viewModelFactories.TablesViewModelFactory;
import com.example.mysql.adapters.ColumnsRecyclerViewAdapter;
import com.example.mysql.adapters.RecordsRecyclerViewAdapterParent;
import com.example.mysql.database.entities.ConnectionEntity;
import com.example.mysql.databinding.FragmentTableRecordsBinding;
import com.example.mysql.models.Cell;
import java.util.ArrayList;



public class TableRecordsFragment extends Fragment {


    private FragmentTableRecordsBinding binding;
    private TablesViewModel viewModel;

    private ArrayList<Cell> columnHeaders;
    private ArrayList<ArrayList<Cell>> rows;

    MyApplication applicationClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTableRecordsBinding.inflate(inflater, container, false);

        init();

        viewModel.getSelectedTable().observe(getViewLifecycleOwner(), s -> binding.tableNameTvRecordsFragment.setText(s));

        viewModel.getSelectedTableData().observe(getViewLifecycleOwner(), data -> {
            if(data != null) {
                columnHeaders = (ArrayList<Cell>) data.get("columns");
                rows = (ArrayList<ArrayList<Cell>>) data.get("records");
                ColumnsRecyclerViewAdapter columnsAdapter;
                if(columnHeaders != null) {
                    columnsAdapter = new ColumnsRecyclerViewAdapter(columnHeaders);

                } else {
                    columnsAdapter = new ColumnsRecyclerViewAdapter(new ArrayList<>());
                }
                binding.columnsRecyclerView.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)));
                binding.columnsRecyclerView.setAdapter(columnsAdapter);
                RecordsRecyclerViewAdapterParent adapter;
                if(rows != null) {
                    adapter = new RecordsRecyclerViewAdapterParent(getContext(), rows);

                } else {
                    adapter = new RecordsRecyclerViewAdapterParent(getContext(), new ArrayList<>());
                }
                binding.recordsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recordsRecyclerView.setAdapter(adapter);
            }
        });


        return binding.getRoot();
    }

    private void init() {
        applicationClass = (MyApplication) getActivity().getApplication();
        columnHeaders = new ArrayList<>();
        rows = new ArrayList<>();

        Bundle bundle = this.getArguments();
        assert bundle != null;
        String password = bundle.getString("password");
        ConnectionEntity currentConnection = new ConnectionEntity(bundle.getString("ipAddress"), bundle.getString("port"), bundle.getString("user"), bundle.getString("database"));

        viewModel = new ViewModelProvider(getActivity(), new TablesViewModelFactory(getActivity().getApplication(),
                applicationClass.getExecutorService(), applicationClass.getMainThreadHandler(), currentConnection.getIpAddress(), currentConnection.getPort(),
                currentConnection.getUser(), password)).get(TablesViewModel.class);
    }

}