package com.bia.bodhi.School;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bia.bodhi.FetchFromDB;
import com.bia.bodhi.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;

public class VideoFragment extends Fragment implements View.OnClickListener {
    String[] Class_list = { "1st", "2nd", "3rd", "4th", "5th","6th","7th","8th","9th","10th","11th","12th" };
    Spinner spin_subjects;
    Uri filePath;
    EditText Video_name,Video_description;
    Button VideoFragmment_upload,pick_video;
    ArrayList<String> SubjectName = new ArrayList();
    ArrayList<String> SubjectID = new ArrayList();
    String ID;
    String Cls;
    private static final int PICK_FROM_GALLERY = 101;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        StartServerFile();
        // Class spinner
        Spinner spin = (Spinner)v. findViewById(R.id.VideoFragment_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cls = Class_list[position];
                Log.e("class",Cls);
                Toast.makeText(getActivity(), "Selected : "+Class_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Subject spinner
        spin_subjects = (Spinner)v. findViewById(R.id.VideoFragment_Subject);

        Video_name = (EditText)v.findViewById(R.id.Video_name);
        Video_description = (EditText)v.findViewById(R.id.Video_description);
        pick_video = (Button)v.findViewById(R.id.pick_video);
        pick_video.setOnClickListener(this);
        VideoFragmment_upload = (Button)v.findViewById(R.id.VideoFragmment_upload);
        VideoFragmment_upload.setOnClickListener(this);
        return v;

    }

    public void onPreServerFile()
    {
        //
    }

    public void StartServerFile()
    {

        String url = "https://bodhi.shwetaaromatics.co.in/SubjectFetch.php";
        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                //this function executes after
                Toast.makeText(getActivity(),"END",Toast.LENGTH_SHORT).show();
                try
                {
                    ConvertFromJSON(output);
                    EndServerFile();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }
    public void EndServerFile()
    {
        ArrayAdapter<String> subject_adaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SubjectName);
        subject_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_subjects.setAdapter(subject_adaptor);
        spin_subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ID =  SubjectID.get(position);
                Log.e("idaayi",ID);
                // Toast.makeText(getActivity(), "Selected : "+subjects_name[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void ConvertFromJSON(String json)
    {
        try
        {
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




    private void UploadFile(Uri path)
    {
        String uploadId = UUID.randomUUID().toString();
        String x = Commons.getPath(path, getActivity());
        Log.e("filepath",x);
        int type = 1;
        try
        {
            String url = "https://bodhi.shwetaaromatics.co.in/School/UploadMedia.php";
            new MultipartUploadRequest(getActivity(), uploadId, url)
                    .addFileToUpload(String.valueOf(x), "Media")
                    .addParameter("MediaName",Video_name.getText().toString())
                    .addParameter("Description",Video_description.getText().toString())
                    .addParameter("SubjectID",ID)
                    .addParameter("Class",Cls)
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
                            Video_description.getText().clear();
                            Video_name.getText().clear();
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
        }
    }

    public void openFolder()
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("video/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult( chooseFile,PICK_FROM_GALLERY);



    }

    @Override
    public void onClick(View v) {
        if(v == VideoFragmment_upload)
        {
            if(Video_name.getText().toString()!=null && !Video_name.getText().toString().trim().equals("")
                    && Video_description.getText().toString()!=null && !Video_description.getText().toString().trim().equals(""))
            {
                UploadFile(filePath);
            }
            else
            {

                Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == pick_video)
        {
            openFolder();
        }
    }
}
