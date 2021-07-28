package com.supun.mcqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<CategoryModel> catList = new ArrayList<>();
    public static int selected_cat_index = 1;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();



        new Thread() {
            public void run() {
                loadData();


            }
        }.start();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

    }

    private void loadData()
    {
        catList.clear();
        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists())
                    {
                        long count = (long)doc.get("COUNT");
                        for (int i=1; i<= count; i++)
                        {
                            String catName = doc.getString("CAT" + String.valueOf(i));
                            String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

                            catList.add(new CategoryModel(catID,catName));
                        }

                        Intent intent = new Intent(MainActivity.this, Dashboard.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Server is Updating! Please check again in few hours.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}