package com.bia.bodhi.Student;

import android.os.Bundle;
import android.widget.ImageView;

import com.bia.bodhi.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomePage extends AppCompatActivity {


    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    String[] VideoLinks = new String[1000];

    String[] VideoDescription= new String[1000];

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        image= findViewById(R.id.demo_imageview);

        for (int i=0; i<3; i++){

            VideoLinks[i]= "https://shwetaaromatics.co.in/ca/video_upload/files/22vmp4";
            VideoDescription[i]= i + " Thumbnail";

        }

        getImages();
    }

    private void getImages() {

        int k=0;

        while(VideoDescription[k]!= null){

            mImageUrls.add(VideoLinks[k]);
            mNames.add(VideoDescription[k]);

            k++;

        }

        initRecyclerView();

    }

    private void initRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapter adapter = new HomePageRecyclerAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

}
