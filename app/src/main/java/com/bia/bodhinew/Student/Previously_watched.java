package com.bia.bodhinew.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.bia.bodhinew.School.ViewStudentShowAdaptor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Previously_watched extends AppCompatActivity {
    String[] Categories = { "Select","Videos", "Books", "Others"};
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
    static ArrayList<Modelclass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previously_watched);
        nodata = (ImageView)findViewById(R.id.nodata);
        StartServerFile();
        // Class spinner
        Spinner spin = (Spinner) findViewById(R.id.Previously_watched_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catgerory = "";
                if(position != 0)
                {
                    catgerory = Categories[position];
                    Log.e("class",catgerory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        list_previously_watched = (ListView)findViewById(R.id.Previously_watched_list);

    }

    public void onPreServerFile()
    {
        //
    }

    public void StartServerFile()
    {

       // String url = "http://bodhi.shwetaaromatics.co.in/Student/PreviouslyWatched.php?UserID="+file_retreive();
        String url = "http://bodhi.shwetaaromatics.co.in/Student/PreviouslyWatched.php?UserID="+38;
        Log.e("url",url);
        com.bia.bodhinew.FetchFromDB asyncTask = (com.bia.bodhinew.FetchFromDB) new com.bia.bodhinew.FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                //this function executes after
                Toast.makeText(getApplicationContext(),"END",Toast.LENGTH_SHORT).show();
                try
                {
                    ConvertFromJSON(output);
                    list = GetPublisherResults();
                    list_previously_watched.setAdapter(new Previously_watched_adaptor(getApplicationContext(), list));
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
                Log.e("name",FileName[i]);
                FileID[i]=obj.getString("ID");
                Log.e("id",FileID[i]);
                FileUrl[i] = obj.getString("URL");
                Log.e("url",FileUrl[i]);
                FileThumbnailUrl[i] = obj.getString("ThumbnailURL");
                Log.e("url",FileUrl[i]);
                FileDescription[i] = obj.getString("Description");
                Log.e("description",FileDescription[i]);
                FileDateTime[i] = obj.getString("DateTime");
                Log.e("datetime",FileDateTime[i]);
                SubjectName[i] = obj.getString("SubjectName");
                SubjectID[i] = obj.getString("SubjectID");
                Type[i] = obj.getString("Type");
                ispublic[i] = obj.getString("isPublic");

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

        while (FileName[k] != null)
        {
            nodata.setVisibility(View.GONE);
            list_previously_watched.setVisibility(View.VISIBLE);
            Modelclass ar1 = new Modelclass();
            ar1.setFile_name(FileName[k]);
            ar1.setFile_description(FileDescription[k]);
            ar1.setDatetime_of_notice(FileDateTime[k]);
            results.add(ar1);

            k++;
        }

        return results;
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
}
