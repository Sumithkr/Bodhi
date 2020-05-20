package com.bia.bodhi.School;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bia.bodhi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.UUID;

import androidx.fragment.app.Fragment;
import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;

public class RevisionFragment extends Fragment implements View.OnClickListener {
    Animation floating_close;
    Animation floating_open;
    Animation floatingrotate_open;
    Animation floatingrotate_close;
    boolean open =true;
    FloatingActionButton main,RevisionFragment_pickvideo,RevisionFragment_pickbook;
    EditText Revision_name,Revision_description;
    Button RevisionFragment_upload;
    String[] Class_list = { "1st", "2nd", "3rd", "4th", "5th","6th","7th","8th","9th","10th","11th","12th" };
    String[] Subject_list = {"English","Hindi","Math","Science","Computer"};
    private static final int PICK_FROM_GALLERY = 101;
    Uri uri =null;
    int l = 1,j = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_revision, container, false);
        // Class spinner
        Spinner spin = (Spinner)v. findViewById(R.id.RevisionFragment_Class);
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
        Spinner spin_subjects = (Spinner)v. findViewById(R.id.RevisionFragment_Subject);
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
         main =(FloatingActionButton)v.findViewById(R.id.main_floating);
         main.setOnClickListener(this);
         RevisionFragment_pickvideo =(FloatingActionButton)v.findViewById(R.id.RevisionFragment_pickvideo);
         RevisionFragment_pickbook =(FloatingActionButton)v.findViewById(R.id.RevisionFragment_pickbook);
         Revision_name = (EditText)v.findViewById(R.id.Revision_name);
         Revision_description = (EditText)v.findViewById(R.id.Revision_description);
        RevisionFragment_upload = (Button) v.findViewById(R.id.RevisionFragment_upload);
        RevisionFragment_upload.setOnClickListener(this);
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
                    .addParameter("publisher",Revision_name.getText().toString())
                    .addParameter("school",Revision_description.getText().toString())
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
                            Revision_name.getText().clear();
                            Revision_description.getText().clear();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK)
        {
            try
            {
                uri = data.getData();
                Toast.makeText(getActivity(), "-" + l, Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (data.getClipData() != null)
                    {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (l = 0; l < count; l++) {
                            Uri imageUri = data.getClipData().getItemAt(l).getUri();
                            uri = data.getData();
                        }
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                    else if (data.getData() != null)
                    {
                        String imagePath = data.getData().getPath();

                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                }
            }catch(Exception e){}
        }

        if (requestCode == 100 && resultCode == RESULT_OK)
        {

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

            }catch(Exception e){}
        }

    }

    public void opendoc()
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/epub+zip");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile,100);
    }


    public void openVideo()
    {
        Intent choosevideo = new Intent(Intent.ACTION_GET_CONTENT);
        choosevideo.setType("video/*");
        choosevideo = Intent.createChooser(choosevideo, "Choose a video");
        startActivityForResult(choosevideo,PICK_FROM_GALLERY);


    }

    @Override
    public void onClick(View v) {
        if(v == main) {
            floating_close = AnimationUtils.loadAnimation(getActivity(), R.anim.floatingaction_close);
            floating_open = AnimationUtils.loadAnimation(getActivity(), R.anim.floatingaction_open);
            floatingrotate_close = AnimationUtils.loadAnimation(getActivity(), R.anim.floatingrotate_close);
            floatingrotate_open = AnimationUtils.loadAnimation(getActivity(), R.anim.floatingrotate_open);
            if (open) {
                RevisionFragment_pickvideo.startAnimation(floating_open);
                RevisionFragment_pickbook.startAnimation(floating_open);
                main.startAnimation(floatingrotate_open);
                RevisionFragment_pickvideo.setClickable(true);
                RevisionFragment_pickbook.setClickable(true);
                RevisionFragment_pickvideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        openVideo();
                    }
                });

                RevisionFragment_pickbook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opendoc();
                    }
                });
                open = false;

            } else {
                RevisionFragment_pickvideo.startAnimation(floating_close);
                RevisionFragment_pickbook.startAnimation(floating_close);
                main.startAnimation(floatingrotate_close);
                RevisionFragment_pickvideo.setClickable(false);
                RevisionFragment_pickbook.setClickable(false);
                open = true;
            }
        }
      if(v == RevisionFragment_upload)
      {
          if(Revision_name.getText().toString()!=null && !Revision_name.getText().toString().trim().equals("")
                  && Revision_description.getText().toString()!=null && Revision_description.getText().toString().trim().equals(""))
          {
              UploadFile(uri);
          }
          else
          {

              Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
          }
      }

    }
}
