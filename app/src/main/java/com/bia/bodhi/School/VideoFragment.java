package com.bia.bodhi.School;

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

import com.bia.bodhi.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;

public class VideoFragment extends Fragment implements View.OnClickListener {
    String[] Class_list = { "1st", "2nd", "3rd", "4th", "5th","6th","7th","8th","9th","10th","11th","12th" };
    String[] Subject_list = {"English","Hindi","Math","Science","Computer"};
    Uri filePath;
    EditText Video_name,Video_description;
    Button VideoFragmment_upload,pick_video;
    private static final int PICK_FROM_GALLERY = 101;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        // Class spinner
        Spinner spin = (Spinner)v. findViewById(R.id.VideoFragment_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Selected : "+Class_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Subject spinner
        Spinner spin_subjects = (Spinner)v. findViewById(R.id.VideoFragment_Subject);
        ArrayAdapter<String> adapter_subjects = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Subject_list);
        adapter_subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_subjects.setAdapter(adapter_subjects);
        spin_subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Selected : "+Subject_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Video_name = (EditText)v.findViewById(R.id.Video_name);
        Video_description = (EditText)v.findViewById(R.id.Video_description);
        pick_video = (Button)v.findViewById(R.id.pick_video);
        pick_video.setOnClickListener(this);
        VideoFragmment_upload = (Button)v.findViewById(R.id.VideoFragmment_upload);
        VideoFragmment_upload.setOnClickListener(this);
        return v;

    }
    private void UploadFile(Uri path)
    {
        String uploadId = UUID.randomUUID().toString();
        String x = Commons.getPath(path, getActivity());

        try
        {
            String url = "https://shwetaaromatics.co.in/ca/admin/upload_book.php";
            new MultipartUploadRequest(getActivity(), uploadId, url)
                    .addFileToUpload(String.valueOf(x), "pdf")
                    .addParameter("publisher",Video_name.getText().toString())
                    .addParameter("school",Video_description.getText().toString())
                    .setNotificationConfig(new UploadNotificationConfig())
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
              && Video_description.getText().toString()!=null && Video_description.getText().toString().trim().equals(""))
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
