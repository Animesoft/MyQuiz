package com.supun.mcqapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static com.supun.mcqapp.MainActivity.catList;

public class Dashboard extends AppCompatActivity {

    private GridView catGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        catGrid = findViewById(R.id.cat_grid_view);



        CatGridAdapter adapter = new CatGridAdapter(catList);
        catGrid.setAdapter(adapter);

    }
}