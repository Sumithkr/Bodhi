package com.bia.bodhinew.School;

import androidx.appcompat.app.AppCompatActivity;
import in.gauriinfotech.commons.Commons;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.bia.bodhinew.utils;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class Notice_post extends AppCompatActivity implements View.OnClickListener {
    String[] Class_list = {"Class", "1", "2", "3", "4", "5","6","7","8","9","10","11","12" };
    EditText Notice;
    Button Doc,Img,Notice_post_post,BackButton;
    private static final int PICK_FROM_GALLERY = 101;
    Uri uri;
    String Cls;
    TextView filekanaam;
    ProgressDialog dialog;
    String check = "no";
    File f;
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
                Cls = "";
                if(position != 0)
                {
                    Cls = Class_list[position];
                    Log.e("class",Cls);
                }
                // Toast.makeText(getApplicationContext(), "Selected : "+Class_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Notice = (EditText)findViewById(R.id.Notice);
        Doc = (Button)findViewById(R.id.pick_doc);
        Doc.setOnClickListener(this);
        Notice_post_post = (Button)findViewById(R.id.Notice_post_post);
        Notice_post_post.setOnClickListener(this);
        filekanaam = (TextView)findViewById(R.id.filekanaam);
        BackButton = (Button)findViewById(R.id.BackButton);
        BackButton.setOnClickListener(this);
    }

    public void progress()
    {
        dialog=new ProgressDialog(Notice_post.this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    private void UploadFile(Uri path)
    {
        String uploadId = UUID.randomUUID().toString();
        //String x = Commons.getPath(path, getApplicationContext());
        String x = utils.getRealPathFromURI_API19(Notice_post.this, path);
        Log.e("filepath",x);
        if (x.contains(".doc") || x.contains(".docx") || x.contains(".pdf") || x.contains(".ppt") || x.contains(".pptx")
                || x.contains(".jpg") || x.contains(".png") || x.contains(".jpeg"))
        {
            try
            {
                String url = "https://bodhi.shwetaaromatics.co.in/School/UploadNotice.php";
                Log.e("url",url);
                progress();
                new MultipartUploadRequest(Notice_post.this, uploadId, url)
                        .addFileToUpload(String.valueOf(x), "Media")
                        .addParameter("NoticeText",Notice.getText().toString())
                        .addParameter("Class",Cls)
                        .addParameter("UserID",file_retreive())
                        //.setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                                Log.e("ERROR",exception+"-----");
                                //Failed
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                Notice.getText().clear();
                                dialog.dismiss();
                                dialog.cancel();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        }).startUpload();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else{
            Toast.makeText(Notice_post.this, "Please Select Image/Doc/ppt Files", Toast.LENGTH_LONG).show();
        }

    }

    private String file_retreive()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput("Bodhi_Login_School");
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int n;
            while (( n = inputStream.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }

            inputStream.close();
            return fileContent.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "error";
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            uri = data.getData();
            String y = utils.getRealPathFromURI_API19(Notice_post.this, uri);
            f = new File(y);
            filekanaam.setText("" + f.getName().trim());
            Log.e("File name", f.getName());
            check = "yes";
        }

    }

    public void opendoc()
    {

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 100);
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(v == Doc)
        {
            opendoc();
        }

        if (v == Notice_post_post)
        {
            if(Notice.getText().toString()!=null && !Notice.getText().toString().trim().equals("")
                    && !Cls.equals("") &&Cls !=null && check.equals("yes"))
            {
                UploadFile(uri);
            }
            else
            {

                Toast.makeText(getApplicationContext(), "Notice is empty", Toast.LENGTH_SHORT).show();
            }
        }
        if (v == BackButton)
        {
           onBackPressed();

        }

    }
}
