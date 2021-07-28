package com.supun.mcqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.supun.mcqapp.MainActivity.catList;
import static com.supun.mcqapp.MainActivity.selected_cat_index;

public class SetsActivity extends AppCompatActivity {

    private GridView sets_grid;
    private FirebaseFirestore firestore;

    //pvt to public static
    public static int category_id;


    public static List<String> setsIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        category_id = getIntent().getIntExtra("CATEGORY_ID",1);

        sets_grid = findViewById(R.id.sets_gridview);
        firestore = FirebaseFirestore.getInstance();

        loadSets();



    }

    public void loadSets()
    {
        setsIDs.clear();
        firestore.collection("QUIZ").document("CAT" + String.valueOf(category_id))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                        long noOfSets = (long)documentSnapshot.get("SETS");
                        for (int i=1; i<= noOfSets; i++)
                        {
                            //String catName = documentSnapshot.getString("CAT" + String.valueOf(i));
                            setsIDs.add(documentSnapshot.getString("SET" + String.valueOf(i)));
                        }
                SetsAdapter adapter = new SetsAdapter(setsIDs.size());
                sets_grid.setAdapter(adapter);

                    }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            SetsActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}