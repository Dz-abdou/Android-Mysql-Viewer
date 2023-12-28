package com.example.mysql.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mysql.MyApplication;
import com.example.mysql.R;
import com.example.mysql.ViewModels.TablesViewModel;
import com.example.mysql.ViewModels.viewModelFactories.TablesViewModelFactory;
import com.example.mysql.adapters.TablesRecyclerViewAdapter;
import com.example.mysql.database.entities.ConnectionEntity;
import com.example.mysql.databinding.FragmentTablesBinding;
import java.util.ArrayList;


public class TablesFragment extends Fragment {

    FragmentTablesBinding binding;
    TablesViewModel viewModel;

    MyApplication applicationClass;
    TablesRecyclerViewAdapter adapter;

    ArrayList<String> tables;
    String password;
    String selectedDatabase;
    ConnectionEntity currentConnection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTablesBinding.inflate(inflater, container, false);

        init();

        viewModel.getSelectedDatabase().observe(getViewLifecycleOwner(), s -> {
            selectedDatabase = s;
            binding.databaseNameTvTablesFragment.setText(s);
        });

        viewModel.getSelectedDatabaseTables().observe(getViewLifecycleOwner(), selectedTables -> {
            tables = selectedTables;
            adapter.upDateDatabasesList(selectedTables);
        });

        adapter.setOnItemClickListener(position -> {
            ViewPager2 viewPager= getActivity().findViewById(R.id.viewPager);
            viewModel.setSelectedTable(tables.get(position));
            binding.progressBarTablesFragment.setVisibility(View.VISIBLE);
            viewModel.setSelectedTableRecordsAndColumns(selectedDatabase, tables.get(position), binding.progressBarTablesFragment, viewPager);

        });

        return binding.getRoot();
    }

    public void init() {
        applicationClass = (MyApplication) getActivity().getApplication();
        Bundle bundle = this.getArguments();
        assert bundle != null;
        password = bundle.getString("password");
        currentConnection = new ConnectionEntity(bundle.getString("ipAddress"), bundle.getString("port"), bundle.getString("user"), bundle.getString("database"));        tables = new ArrayList<>();
        binding.databasesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TablesRecyclerViewAdapter();
        binding.databasesRecyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity(), new TablesViewModelFactory(getActivity().getApplication(),
                applicationClass.getExecutorService(), applicationClass.getMainThreadHandler(), currentConnection.getIpAddress(), currentConnection.getPort(),
                currentConnection.getUser(),password)).get(TablesViewModel.class);
    }

}