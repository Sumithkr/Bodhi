package com.bia.bodhinew.School;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.UUID;

import androidx.fragment.app.Fragment;
import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;

public class RevisionFragment extends Fragment implements View.OnClickListener {

    EditText Revision_name,Revision_description;
    Button RevisionFragment_upload ,pick_file;
    String[] Class_list = { "Class","1", "2", "3", "4", "5","6","7","8","9","10","11","12" };
    ArrayList<String> SubjectName = new ArrayList();
    ArrayList<String> SubjectID = new ArrayList();
    String ID;
    String Cls;
    Uri uri,thumbnailuri;
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
                Cls = "";
                if(position != 0)
                {
                    Cls = Class_list[position];
                    Log.e("class",Cls);
                }
                //Toast.makeText(getActivity(), "Selected : "+Class_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Subject spinner
        spin_subjects = (Spinner)v. findViewById(R.id.RevisionFragment_Subject);

        pick_file =(Button) v.findViewById(R.id.pick_File);
        pick_file.setOnClickListener(this);
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
                ID="";
                if(position != 0) {
                    ID = SubjectID.get(position);
                    Log.e("idaayi", ID);
                }
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void UploadFile(Uri path)
    {
        String uploadId = UUID.randomUUID().toString();
        String x = Commons.getPath(path, getActivity());
        Log.e("filepath",x);
        int type = 2;
        if (x.contains(".doc") || x.contains(".docx") || x.contains(".pdf") || x.contains(".ppt") || x.contains(".pptx")
          || x.contains(".mp4") || x.contains(".3gp") || x.contains(".mp3") || x.contains(".jpg") || x.contains(".png") || x.contains(".jpeg"))
        {
            if (x.contains(".mp4") || x.contains(".3gp"))
            {
                String thumbpath = null;
                try
                {
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(x, MediaStore.Video.Thumbnails.MICRO_KIND);
                    thumbnailuri = getImageUri(getActivity(),thumb);
                    Log.e("thumbnail uri", String.valueOf(thumbnailuri));
                    thumbpath = Commons.getPath(thumbnailuri,getActivity());
                    Log.e("thumbnailpath", thumbpath);
                    Log.e("video thumbnail", String.valueOf(thumb));
                    //Intent intent = new Intent(getActivity(), test.class);
                    //intent.putExtra("bitmap", thumb);
                    //startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();

                }
                try {
                    String url = "https://bodhi.shwetaaromatics.co.in/School/UploadMedia.php";
                    Log.e("Url",url);
                    Log.e("check","video");
                    new MultipartUploadRequest(getActivity(), uploadId, url)
                            .addFileToUpload(String.valueOf(x), "Media")
                            .addFileToUpload(String.valueOf(thumbpath), "Thumbnail")
                            .addParameter("MediaName", Revision_name.getText().toString())
                            .addParameter("Description", Revision_description.getText().toString())
                            .addParameter("SubjectID", ID)
                            .addParameter("Class", Cls)
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
                                    Log.e("ERROR", exception + "-----");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    String url = "https://bodhi.shwetaaromatics.co.in/School/UploadMedia.php";
                    Log.e("Url",url);
                    Log.e("check","others");
                    new MultipartUploadRequest(getActivity(), uploadId, url)
                            .addFileToUpload(String.valueOf(x), "Media")
                            .addParameter("MediaName", Revision_name.getText().toString())
                            .addParameter("Description", Revision_description.getText().toString())
                            .addParameter("SubjectID", ID)
                            .addParameter("Class", Cls)
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
                                    Log.e("ERROR", exception + "-----");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(getActivity(), "Please Select Video/Audio/Image/Doc/ppt Files", Toast.LENGTH_LONG).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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


    public void openfile()
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile,100);
    }


    @Override
    public void onClick(View v) {
        if(v == pick_file) {
            openfile();
        }
        if(v == RevisionFragment_upload)
        {
            if(Revision_name.getText().toString()!=null && !Revision_name.getText().toString().trim().equals("")
                    && Revision_description.getText().toString()!=null && !Revision_description.getText().toString().trim().equals("")
            && Cls!=null && ID!=null && !Cls.equals("") && !ID.equals(""))
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
