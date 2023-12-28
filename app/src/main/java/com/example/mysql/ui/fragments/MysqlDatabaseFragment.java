package com.example.mysql.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mysql.MyApplication;
import com.example.mysql.R;
import com.example.mysql.ViewModels.TablesViewModel;
import com.example.mysql.ViewModels.viewModelFactories.TablesViewModelFactory;
import com.example.mysql.adapters.DatabasesRecyclerViewAdapter;
import com.example.mysql.database.entities.ConnectionEntity;
import com.example.mysql.databinding.FragmentMysqlDatabaseBinding;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MysqlDatabaseFragment extends Fragment implements View.OnClickListener {

    MyApplication applicationClass;

    private ArrayList<String> databases;
    private FragmentMysqlDatabaseBinding binding;
    private TablesViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMysqlDatabaseBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(){
        applicationClass = (MyApplication) getActivity().getApplication();
        databases = new ArrayList<>();
        databases = new ArrayList<>();
        ViewPager2 viewPager=(ViewPager2) getActivity().findViewById(R.id.viewPager);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String password = bundle.getString("password");
        ConnectionEntity currentConnection = new ConnectionEntity(bundle.getString("ipAddress"), bundle.getString("port"), bundle.getString("user"), bundle.getString("database"));
        databases = bundle.getStringArrayList("databases");
        viewModel = new ViewModelProvider(getActivity(), new TablesViewModelFactory(getActivity().getApplication(),
                applicationClass.getExecutorService(), applicationClass.getMainThreadHandler(), currentConnection.getIpAddress(), currentConnection.getPort(), currentConnection.getUser(),
                password)).get(TablesViewModel.class);

        binding.databasesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabasesRecyclerViewAdapter adapter = new DatabasesRecyclerViewAdapter(databases);
        binding.databasesRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(position -> {
            viewModel.setSelectedDatabase(databases.get(position));
            binding.progressBarDatabasesFragment.setVisibility(View.VISIBLE);
            viewModel.setSelectedDatabaseTables(databases.get(position), binding.progressBarDatabasesFragment, viewPager);
        });
    }

    @Override
    public void onClick(View view) {

    }
}