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
    int l = 1,j = 1;
    String Cls;
    ProgressDialog dialog;
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
        BackButton = (Button)findViewById(R.id.BackButton);
        BackButton.setOnClickListener(this);
    }

    public void progress()
    {
        dialog=new ProgressDialog(getApplicationContext());
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    private void UploadFile(Uri path)
    {
        progress();
        String uploadId = UUID.randomUUID().toString();
        //String x = Commons.getPath(path, getApplicationContext());
        String x = utils.getRealPathFromURI_API19(getApplicationContext(), path);
        Log.e("filepath",x);
        try
        {
            String url = "https://bodhi.shwetaaromatics.co.in/School/UploadNotice.php";
            new MultipartUploadRequest(getApplicationContext(), uploadId, url)
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
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                uri = data.getData();
                Toast.makeText(getApplicationContext(), "-" + l, Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (l = 0; l < count; l++) {
                            Uri imageUri = data.getClipData().getItemAt(l).getUri();
                            uri = data.getData();
                        }
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    } else if (data.getData() != null) {
                        String imagePath = data.getData().getPath();

                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                }
            } catch (Exception e) {
            }
        }

        if (requestCode == 100 && resultCode == RESULT_OK) {

            try {

                uri = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (j = 0; j < count; j++) {
                            Uri docUri = data.getClipData().getItemAt(j).getUri();
                            uri = data.getData();
                        }
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    } else if (data.getData() != null) {
                        String docPath = data.getData().getPath();
                        //do something with the image (save it to some directory or whatever you need to do with it here
                    }
                }

            } catch (Exception e) {
            }
        }

    }

    public void opendoc()
    {
        String[] mimeTypes =
                {"application/pdf","application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document","image/*"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), 100);
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
                    && !Cls.equals("") &&Cls !=null)
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
