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

import com.bia.bodhi.FetchFromDB;
import com.bia.bodhi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
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
    FloatingActionButton main,RevisionFragment_pickvideo,RevisionFragment_pickbook,RevisionFragment_pickaudio;
    EditText Revision_name,Revision_description;
    Button RevisionFragment_upload;
    String[] Class_list = { "1", "2", "3", "4", "5","6","7","8","9","10","11","12" };
    ArrayList<String> SubjectName = new ArrayList();
    ArrayList<String> SubjectID = new ArrayList();
    String ID;
    String Cls;
    private static final int PICK_FROM_GALLERY = 101;
    Uri uri;
    int l = 1,j = 1;
    Spinner spin_subjects;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_revision, container, false);
        StartServerFile();
        // Class spinner
        Spinner spin = (Spinner)v. findViewById(R.id.RevisionFragment_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cls = Class_list[position];
                Log.e("class",Cls);
                //Toast.makeText(getActivity(), "Selected : "+Class_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Subject spinner
        spin_subjects = (Spinner)v. findViewById(R.id.RevisionFragment_Subject);

        main =(FloatingActionButton)v.findViewById(R.id.main_floating);
        main.setOnClickListener(this);
        RevisionFragment_pickvideo =(FloatingActionButton)v.findViewById(R.id.RevisionFragment_pickvideo);
        RevisionFragment_pickbook =(FloatingActionButton)v.findViewById(R.id.RevisionFragment_pickbook);
        RevisionFragment_pickaudio =(FloatingActionButton)v.findViewById(R.id.RevisionFragment_pickaudio);
        Revision_name = (EditText)v.findViewById(R.id.Revision_name);
        Revision_description = (EditText)v.findViewById(R.id.Revision_description);
        RevisionFragment_upload = (Button) v.findViewById(R.id.RevisionFragment_upload);
        RevisionFragment_upload.setOnClickListener(this);
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
        int type = 2;
        try
        {
            String url = "https://bodhi.shwetaaromatics.co.in/School/UploadMedia.php";
            new MultipartUploadRequest(getActivity(), uploadId, url)
                    .addFileToUpload(String.valueOf(x), "Media")
                    .addParameter("MediaName",Revision_name.getText().toString())
                    .addParameter("Description",Revision_description.getText().toString())
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
                            Revision_description.getText().clear();
                            Revision_name.getText().clear();
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

        //for video
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

        //for doc
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

        //for audio
        if (requestCode == 200 && resultCode == RESULT_OK)
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

    public void openaudio()
    {
        Intent chooseAudio = new Intent(Intent.ACTION_GET_CONTENT);
        chooseAudio.setType("audio/*");
        chooseAudio = Intent.createChooser(chooseAudio, "Choose a Audio");
        startActivityForResult(chooseAudio,200);
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
                RevisionFragment_pickaudio.startAnimation(floating_open);
                main.startAnimation(floatingrotate_open);
                RevisionFragment_pickvideo.setClickable(true);
                RevisionFragment_pickbook.setClickable(true);
                RevisionFragment_pickaudio.setClickable(true);
                RevisionFragment_pickaudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openaudio();
                    }
                });
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
                RevisionFragment_pickaudio.startAnimation(floating_close);
                main.startAnimation(floatingrotate_close);
                RevisionFragment_pickvideo.setClickable(false);
                RevisionFragment_pickbook.setClickable(false);
                RevisionFragment_pickaudio.setClickable(false);
                open = true;
            }
        }
        if(v == RevisionFragment_upload)
        {
            if(Revision_name.getText().toString()!=null && !Revision_name.getText().toString().trim().equals("")
                    && Revision_description.getText().toString()!=null && !Revision_description.getText().toString().trim().equals(""))
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
