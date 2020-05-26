package com.bia.bodhinew.Student;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.bia.bodhinew.R;

import androidx.appcompat.app.AppCompatActivity;

public class test extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        imageView = (ImageView )findViewById(R.id.imageView);
        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        imageView.setImageBitmap(bitmap);
    }
}
