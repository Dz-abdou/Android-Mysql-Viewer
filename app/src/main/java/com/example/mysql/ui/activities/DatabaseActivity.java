package com.example.mysql.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.mysql.MyApplication;
import com.example.mysql.R;
import com.example.mysql.ViewModels.TablesViewModel;
import com.example.mysql.ViewModels.viewModelFactories.TablesViewModelFactory;
import com.example.mysql.adapters.PagerAdapter;
import com.example.mysql.database.entities.ConnectionEntity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final int NUM_TABS = 3;
    private FragmentStateAdapter pagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private final int[] tabsIcons = {R.drawable.ic_database, R.drawable.ic_baseline_table_rows_24, R.drawable.ic_baseline_grid_on_24};
    MyApplication applicationClass;

    TablesViewModel viewModel;
    private ConnectionEntity currentConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        init();

        viewModel.setSelectedDatabase(currentConnection.getDbName());
        viewModel.setSelectedDatabaseTables(currentConnection.getDbName(), null, null);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> tab.setIcon(tabsIcons[position])).attach();

        toolbar.setNavigationOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void init() {
        applicationClass = (MyApplication) getApplicationContext();
        toolbar = findViewById(R.id.databaseActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentConnection = new ConnectionEntity(getIntent().getStringExtra("ipAddress"),
                getIntent().getStringExtra("port"), getIntent().getStringExtra("user"),
                getIntent().getStringExtra("database"));
        String password = getIntent().getStringExtra("password");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.columnHeaderLightGrayText));
        toolbar.setSubtitle(currentConnection.getIpAddress() + ":" + currentConnection.getPort());
        viewModel = new ViewModelProvider(DatabaseActivity.this, new TablesViewModelFactory(this.getApplication(),applicationClass.getExecutorService() , applicationClass.getMainThreadHandler(), currentConnection.getIpAddress(),
                currentConnection.getPort(), currentConnection.getUser(), password)).get(TablesViewModel.class);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle(), NUM_TABS, currentConnection, password,
                getIntent().getStringArrayListExtra("databases"), getIntent().getStringArrayListExtra("tables"));
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

}