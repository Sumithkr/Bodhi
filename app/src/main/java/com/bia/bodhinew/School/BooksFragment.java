package com.bia.bodhinew.School;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;
import com.bia.bodhinew.utils;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;

public class BooksFragment extends Fragment implements View.OnClickListener {
    EditText Book_name,Book_description;
    Button pick_book,BooksFragment_upload;
    String[] Class_list = {"Class", "1", "2", "3", "4", "5","6","7","8","9","10","11","12" };
    private static final int PICK_FROM_GALLERY = 101;
    ArrayList<String> SubjectName = new ArrayList();
    ArrayList<String> SubjectID = new ArrayList();
    private Spinner spin_subjects;
    String ID;
    String Cls;
    Uri filePath = null;
    String check = "no";
    ACProgressPie dialog;
    TextView filekanaam;
    File f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_books, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            String output = bundle.getString("output");
            ConvertFromJSON(output);
        }
        // Class spinner
        Spinner spin = (Spinner)v. findViewById(R.id.BooksFragment_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Class_list);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Subject spinner
        spin_subjects = (Spinner)v. findViewById(R.id.BooksFragment_Subject);
        ArrayAdapter<String> subject_adaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SubjectName);
        subject_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_subjects.setAdapter(subject_adaptor);
        spin_subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ID = "";
                if(position != 0) {
                    ID = SubjectID.get(position);
                    Log.e("idaayi", ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Book_name = (EditText)v.findViewById(R.id.Book_name);
        Book_description = (EditText)v.findViewById(R.id.Book_description);
        pick_book = (Button)v.findViewById(R.id.pick_book);
        pick_book.setOnClickListener(this);
        BooksFragment_upload = (Button)v.findViewById(R.id.BooksFragment_upload);
        BooksFragment_upload.setOnClickListener(this);
        filekanaam = (TextView)v.findViewById(R.id.filekanaam);
        return v;
    }

    private void ConvertFromJSON(String json)
    {
        try
        {
            SubjectName.add("Subject");
            SubjectID.add(String.valueOf(0));
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                SubjectName.add(obj.getString("SubjectName"));
                SubjectID.add(obj.getString("SubjectID"));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void progress()
    {
        dialog = new ACProgressPie.Builder(getActivity())
                .ringColor(Color.parseColor("#fa3a0f"))
                .pieColor(Color.parseColor("#fa3a0f"))
                .bgAlpha(1)
                .bgColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.show();
        dialog.setCancelable(false);
    }


    private String file_retreive()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = getActivity().openFileInput("Bodhi_Login_School");
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

    private void UploadFile(Uri path)
    {
        progress();
        String uploadId = UUID.randomUUID().toString();
        //String x = Commons.getPath(path, getActivity());
        String x = utils.getRealPathFromURI_API19(getActivity(), path);
        /*String file;
        if (filename.indexOf(".") > 0) {
            file = filename.substring(0, filename.lastIndexOf("."));
        } else {
            file =  filename;
        }
        Log.e("real path",x);
        Log.e("Name with extension",filename);
        Log.e("Name without extention ",file);*/
        Log.e("filepath",x);
        int type = 0;
        try
        {
            String url = "https://bodhi.shwetaaromatics.co.in/School/UploadMedia.php";
            new MultipartUploadRequest(getActivity(), uploadId, url)
                    .addFileToUpload(String.valueOf(x), "Media")
                    .addParameter("MediaName",Book_name.getText().toString())
                    .addParameter("Description",Book_description.getText().toString())
                    .addParameter("SubjectID",ID)
                    .addParameter("Class",Cls)
                    .addParameter("UserID", file_retreive())
                    //.addParameter("Media",file)
                    .addParameter("Type", String.valueOf(type))
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
                            Book_description.getText().clear();
                            Book_name.getText().clear();
                            filekanaam.setText("Select file");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            String y = utils.getRealPathFromURI_API19(getActivity(), filePath);
            f = new File(y);
            filekanaam.setText(""+f.getName().trim());
            Log.e("File name", f.getName());
            check = "yes";
        }
    }

    public void openFolder()
    {
        Intent chooseFile = new Intent(Intent. ACTION_GET_CONTENT );
        chooseFile.setType("application/epub+zip");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult( chooseFile,PICK_FROM_GALLERY);
    }

    @Override
    public void onClick(View v) {
        if(v == BooksFragment_upload)
        {
            if(Book_name.getText().toString()!=null && !Book_name.getText().toString().trim().equals("")
                    && Book_description.getText().toString()!=null && !Book_description.getText().toString().trim().equals("")
                    && Cls!=null && ID!=null && !Cls.equals("") && !ID.equals("") && check.equals("yes"))
            {
                UploadFile(filePath);
            }
            else
            {
                Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == pick_book)
        {
            openFolder();
        }
    }

}
