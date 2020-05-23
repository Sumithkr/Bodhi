package com.bia.bodhi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bia.bodhi.School.Master_activity;
import com.bia.bodhi.Student.MasterStudentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class choose extends AppCompatActivity implements View.OnClickListener {
    ImageView Student,School;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        isStoragePermissionGranted();
        Student = (ImageView)findViewById(R.id.Student);
        Student.setOnClickListener(this);
        School = (ImageView)findViewById(R.id.School);
        School.setOnClickListener(this);
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(jbjbj,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           // Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
        else{
        }
    }

    @Override
    public void onClick(View v) {
        if(v == Student)
        {

            startActivity(new Intent(choose.this, MasterStudentActivity.class));
        }

        if(v == School)
        {

            startActivity(new Intent(choose.this, Master_activity.class));
        }
    }
}
