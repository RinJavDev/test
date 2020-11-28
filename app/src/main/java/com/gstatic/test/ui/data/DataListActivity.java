package com.gstatic.test.ui.data;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gstatic.test.R;
import com.gstatic.test.pojo.LocationData;
import com.gstatic.test.ui.map.MapsActivity;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataListActivity extends AppCompatActivity implements RecyclerViewAdapter.RecycleViewInterface, MyViewModel.OnErrorListener {
    @BindView(R.id.locationDataRecyclerView)
    RecyclerView locationDataRecyclerView;

    @BindView(R.id.loadingLayout)
    LinearLayout loadingLayout;

    MyViewModel model;
    RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        ButterKnife.bind(this);

        setTitle("Data list");

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerViewAdapter.setOnLoadMoreListener(this);
        locationDataRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        locationDataRecyclerView.setAdapter(recyclerViewAdapter);



         model = ViewModelProviders.of(this).get(MyViewModel.class);
         model.setCode(getIntent().getStringExtra("code"));
         model.setOnError(this::onError);

         LiveData<List<LocationData>> data = model.getData();

         data.observe(this, new Observer<List<LocationData>>() {
            @Override
            public void onChanged(@Nullable List<LocationData> locationData) {
                Log.d("onChanged" ,String.valueOf(locationData.size()));
                recyclerViewAdapter.setData(locationData);
                loadingLayout.setVisibility(View.GONE);
            }
         });

    }

    @Override
    public void onLoadMore(int page) {
                loadingLayout.setVisibility(View.VISIBLE);
                model.loadData(page);
            }

    @Override
    public void onItemClick(LocationData locationData) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id",locationData.getId());
        intent.putExtra("name",locationData.getName());
        intent.putExtra("country",locationData.getCountry());
        intent.putExtra("lat",locationData.getLat());
        intent.putExtra("lon",locationData.getLon());
        startActivity(intent);
    }

    @Override
    public void onError(String string) {
        loadingLayout.setVisibility(View.GONE);
        Toast.makeText(this,string,  Toast.LENGTH_LONG).show();
    }
}
