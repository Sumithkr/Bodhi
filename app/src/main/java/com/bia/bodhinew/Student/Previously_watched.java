package com.bia.bodhinew.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoInfo;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;
import com.bia.bodhinew.School.Modelclass;
import com.folioreader.FolioReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class Previously_watched extends AppCompatActivity {
    String[] Categories = { "All","Videos", "Books", "Others"};
    String catgerory;
    static ImageView nodata;
    static ListView list_previously_watched;
    static String[] FileName = new String[1000];
    static String[] FileID = new String[1000];
    static String[] FileUrl = new String[1000];
    static String[] FileThumbnailUrl = new String[1000];
    static String[] FileDescription = new String[1000];
    static String[] FileDateTime = new String[1000];
    static String[] SubjectName = new String[1000];
    static String[] SubjectID = new String[1000];
    static String[] ispublic = new String[1000];
    static String[] Type = new String[1000];
    static ArrayList<Modelclass> BookList;
    static ArrayList<Modelclass> resultsBooks= new ArrayList<>();
    static ArrayList<Modelclass> VideoList;
    static ArrayList<Modelclass> resultsVideos= new ArrayList<>();
    static ArrayList<Modelclass> OthersList;
    static ArrayList<Modelclass> resultsOthers= new ArrayList<>();
    static ArrayList<Modelclass> list;
    ACProgressPie dialog;
    FolioReader folioReader = FolioReader.get();
    String file_substring;
    static int universal=0;
    //ProgressDialog download_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previously_watched);
        nodata = (ImageView)findViewById(R.id.nodata);
        StartServerFile();
        Spinner spin = (Spinner) findViewById(R.id.Previously_watched_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        list_previously_watched = (ListView)findViewById(R.id.Previously_watched_list);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                catgerory = "";

                catgerory = Categories[position];

                if(catgerory.equals("All"))
                {
                    list_previously_watched.setAdapter(new Previously_watched_adaptor(Previously_watched.this, list));
                }
                else if(catgerory.equals("Videos"))
                {
                    list_previously_watched.setAdapter(new Previously_watched_adaptor(Previously_watched.this, VideoList));
                }
                else if(catgerory.equals("Books"))
                {
                    list_previously_watched.setAdapter(new Previously_watched_adaptor(Previously_watched.this, BookList));
                }
                else if(catgerory.equals("Others"))
                {
                    list_previously_watched.setAdapter(new Previously_watched_adaptor(Previously_watched.this, OthersList));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void StartServerFile()
    {
         progdialog();
        String url = "https://bodhi.shwetaaromatics.co.in/Student/PreviouslyWatched.php?UserID="+file_retreive();
        com.bia.bodhinew.FetchFromDB asyncTask = (com.bia.bodhinew.FetchFromDB) new com.bia.bodhinew.FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                try
                {
                    ConvertFromJSON(output);
                    PostExecute();
                    dialog.dismiss();
                    dialog.cancel();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    private void ConvertFromJSON(String json)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                FileName[i]=obj.getString("Name");
                Log.e("filename",FileName[i]);
                FileID[i]=obj.getString("ID");
                FileUrl[i] = obj.getString("URL");
                FileThumbnailUrl[i] = obj.getString("ThumbnailURL");
                FileDescription[i] = obj.getString("Description");
                FileDateTime[i] = obj.getString("DateTime");
                SubjectName[i] = obj.getString("SubjectName");
                SubjectID[i] = obj.getString("SubjectID");
                Type[i] = obj.getString("Type");
                Log.e("Type",Type[i]);
                ispublic[i] = obj.getString("isPublic");

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dialog.dismiss();
        dialog.cancel();
    }

    public void PostExecute()
    {

        list = GetPublisherResults();
        list_previously_watched.setAdapter(new Previously_watched_adaptor(Previously_watched.this, list));

        while(Type[universal]!= null){

            if(Type[universal].equals("0")) {

                BookList = GetBooksDetailing();

            }

            if(Type[universal].equals("1")) {

                VideoList = GetVideoDetailing();

            }

            if(Type[universal].equals("2")) {

                OthersList = GetOtherDetailing();

            }

            universal++;
        }

    }

    private static ArrayList<Modelclass> GetBooksDetailing()
    {

        Modelclass modelclass = new Modelclass();
        modelclass.setFile_name(FileName[universal]);
        Log.e("Book Name",FileUrl[universal]);
        modelclass.setFileURL(FileUrl[universal]);
        modelclass.setFile_description(FileDescription[universal]);
        modelclass.setDatetime_of_notice(FileDateTime[universal]);
        modelclass.setSubject_name(SubjectName[universal]);
        modelclass.setImg_of_notice(FileThumbnailUrl[universal]);
        if (FileThumbnailUrl[universal].equals("") || FileThumbnailUrl[universal] == null)
        {
            modelclass.setBoolImage(false);
        }
        else {
            modelclass.setBoolImage(true);
        }

        resultsBooks.add(modelclass);

        return resultsBooks;
    }

    private static ArrayList<Modelclass> GetVideoDetailing()
    {

        Modelclass modelclass = new Modelclass();
        modelclass.setFile_name(FileName[universal]);
        Log.e("Video Name",FileUrl[universal]);
        modelclass.setFileURL(FileUrl[universal]);
        modelclass.setFile_description(FileDescription[universal]);
        modelclass.setDatetime_of_notice(FileDateTime[universal]);
        modelclass.setSubject_name(SubjectName[universal]);
        modelclass.setImg_of_notice(FileThumbnailUrl[universal]);

        if (FileThumbnailUrl[universal].equals("") || FileThumbnailUrl[universal] == null)
        {
            modelclass.setBoolImage(false);
        }
        else {
            modelclass.setBoolImage(true);
        }

        resultsVideos.add(modelclass);

        return resultsVideos;
    }

    private static ArrayList<Modelclass> GetOtherDetailing()
    {

        Modelclass modelclass = new Modelclass();
        modelclass.setFile_name(FileName[universal]);
        modelclass.setFileURL(FileUrl[universal]);
        modelclass.setFile_description(FileDescription[universal]);
        modelclass.setDatetime_of_notice(FileDateTime[universal]);
        modelclass.setSubject_name(SubjectName[universal]);
        modelclass.setImg_of_notice(FileThumbnailUrl[universal]);
        if (FileThumbnailUrl[universal].equals("") || FileThumbnailUrl[universal] == null)
        {
            modelclass.setBoolImage(false);
        }
        else {
            modelclass.setBoolImage(true);
        }

        resultsOthers.add(modelclass);

        return resultsOthers;
    }

    private static ArrayList<Modelclass> GetPublisherResults()
    {
        ArrayList<Modelclass> results = new ArrayList<>();
        int k =0;
        if(FileName[k] == null)
        {
            nodata.setVisibility(View.VISIBLE);
            list_previously_watched.setVisibility(View.GONE);
        }
        else {
            nodata.setVisibility(View.GONE);
            list_previously_watched.setVisibility(View.VISIBLE);
        }

        while (FileName[k] != null) {
            Modelclass ar1 = new Modelclass();
            ar1.setFile_name(FileName[k]);
            ar1.setFileURL(FileUrl[k]);
            ar1.setFile_description(FileDescription[k]);
            ar1.setDatetime_of_notice(FileDateTime[k]);
            ar1.setSubject_name(SubjectName[k]);
            ar1.setImg_of_notice(FileThumbnailUrl[k]);

            if (FileThumbnailUrl[k].equals("") || FileThumbnailUrl[k] == null)
            {
                ar1.setBoolImage(false);
            }
            else {
                ar1.setBoolImage(true);
            }

            k++;
            results.add(ar1);
        }

        return results;
    }

    public void progdialog()
    {
        dialog = new ACProgressPie.Builder(this)
                .ringColor(Color.parseColor("#fa3a0f"))
                .pieColor(Color.parseColor("#fa3a0f"))
                .bgAlpha(1)
                .bgColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.show();
    }


    private String file_retreive()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput("Bodhi_Login");
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

}
