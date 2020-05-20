package com.bia.bodhi.School;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bia.bodhi.R;

public class Notice_post extends AppCompatActivity implements View.OnClickListener {
    String[] Class_list = { "1st", "2nd", "3rd", "4th", "5th","6th","7th","8th","9th","10th","11th","12th" };
    EditText Notice;
    Button Doc,Img,Notice_post_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_post);
        Spinner spin = (Spinner) findViewById(R.id.Notice_post_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Selected : "+Class_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Notice = (EditText)findViewById(R.id.Notice);
        Doc = (Button)findViewById(R.id.pick_doc);
        Doc.setOnClickListener(this);
        Img = (Button)findViewById(R.id.pick_img);
        Img.setOnClickListener(this);
        Notice_post_post = (Button)findViewById(R.id.Notice_post_post);
        Notice_post_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
