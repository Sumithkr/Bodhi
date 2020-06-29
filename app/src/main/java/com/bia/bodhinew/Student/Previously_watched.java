package com.bia.bodhinew.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import com.bia.bodhinew.School.SchoolNoticeShowAdaptor;
import com.bia.bodhinew.School.ViewStudentShowAdaptor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
                    ArrayList<String> extesnion = new ArrayList<>();

                    catgerory = Categories[position];
                    if(catgerory.equals("Videos"))
                    {
                        extesnion.add(".mp4");
                        extesnion.add(".3gp");
                        list = GetPublisherResults(extesnion);
                    }
                    else if(catgerory.equals("Books"))
                    {
                        extesnion.add(".epub");
                        list = GetPublisherResults(extesnion);
                    }
                    else
                    {
                        extesnion.add(".mp4");
                        extesnion.add(".3gp");
                        extesnion.add(".epub");

                        list = GetPublisherResults(extesnion,1);
                    }

                    list_previously_watched.setAdapter(new Previously_watched_adaptor(getApplicationContext(), list));
                    Log.e("category",catgerory);

                    //Call Check File Function with parameter catgerory

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        list_previously_watched = (ListView)findViewById(R.id.Previously_watched_list);
        list_previously_watched.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fileurl = FileUrl[i];
                Toast.makeText(Previously_watched.this, "url"+fileurl, Toast.LENGTH_SHORT).show();
                new DownloadFileFromURL().execute(fileurl);
            }
        });

    }

    public void onPreServerFile()
    {
        //
    }

    public void StartServerFile()
    {

        String url = "https://bodhi.shwetaaromatics.co.in/Student/PreviouslyWatched.php?UserID="+file_retreive();
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
                FileID[i]=obj.getString("ID");
                FileUrl[i] = obj.getString("URL");
                FileThumbnailUrl[i] = obj.getString("ThumbnailURL");
                FileDescription[i] = obj.getString("Description");
                FileDateTime[i] = obj.getString("DateTime");
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
    private static ArrayList<Modelclass> GetPublisherResults(ArrayList<String> extension,int o)
    {
        ArrayList<Modelclass> results = new ArrayList<>();

        int k =0;
        if(FileName[k] == null)
        {
            nodata.setVisibility(View.VISIBLE);
            list_previously_watched.setVisibility(View.GONE);
        }
        else
        {
            nodata.setVisibility(View.GONE);
            list_previously_watched.setVisibility(View.VISIBLE);
        }

        while (FileName[k] != null)
        {
            int extensionCount = 0;
            Boolean FileShow = true;

            for(extensionCount =0;extensionCount<extension.size();extensionCount++)
            {
                if(FileUrl[k].contains(extension.get(extensionCount)))
                {
                    FileShow = false;
                    break;
                }

            }


            Modelclass ar1 = new Modelclass();
            if(FileShow)
            {

                ar1.setFile_name(FileName[k]);
                ar1.setFile_description(FileDescription[k]);
                ar1.setDatetime_of_notice(FileDateTime[k]);
                ar1.setSubject_name(SubjectName[k]);
                ar1.setImg_of_notice(FileThumbnailUrl[k]);
                results.add(ar1);
            }

            if (FileThumbnailUrl[k].equals("") || FileThumbnailUrl[k] == null)
            {
                ar1.setBoolImage(false);
            }
            else {
              ar1.setBoolImage(true);
            }


            k++;
        }

        return results;
    }
    private static ArrayList<Modelclass> GetPublisherResults(ArrayList<String> extension)
    {
        ArrayList<Modelclass> results = new ArrayList<>();

        int k =0;
        if(FileName[k] == null)
        {
            nodata.setVisibility(View.VISIBLE);
            list_previously_watched.setVisibility(View.GONE);
        }
        else
        {
            nodata.setVisibility(View.GONE);
            list_previously_watched.setVisibility(View.VISIBLE);
        }

        while (FileName[k] != null)
        {
            int extensionCount = 0;
            Boolean FileShow = false;

            for(extensionCount =0;extensionCount<extension.size();extensionCount++)
            {
                if(FileUrl[k].contains(extension.get(extensionCount)))
                {
                    FileShow = true;
                    break;
                }

            }
            Modelclass ar1 = new Modelclass();
            if(FileShow)
            {
                ar1.setFile_name(FileName[k]);
                ar1.setFile_description(FileDescription[k]);
                ar1.setDatetime_of_notice(FileDateTime[k]);
                ar1.setSubject_name(SubjectName[k]);
                ar1.setImg_of_notice(FileThumbnailUrl[k]);
                results.add(ar1);
            }

            if (FileThumbnailUrl[k].equals("") || FileThumbnailUrl[k] == null)
            {
                ar1.setBoolImage(false);
            }
            else {
                ar1.setBoolImage(true);
            }

            k++;
        }

        return results;
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
        Modelclass ar1 = new Modelclass();
        while (FileName[k] != null)
        {
            ar1.setFile_name(FileName[k]);
            ar1.setFile_description(FileDescription[k]);
            ar1.setDatetime_of_notice(FileDateTime[k]);
            ar1.setSubject_name(SubjectName[k]);
            ar1.setImg_of_notice(FileThumbnailUrl[k]);
            results.add(ar1);

            if (FileThumbnailUrl[k].equals("") || FileThumbnailUrl[k] == null)
            {
                ar1.setBoolImage(false);
            }
            else {
                ar1.setBoolImage(true);
            }

            k++;
        }

        return results;
    }

    private void CheckFile( String filePath)
    {
        String url = filePath;

        Log.e("urlincheck",url);
        String dataType="";


        if (url.toString().contains(".doc") || url.toString().contains(".docx"))
        {
            // Word document
            dataType = "application/msword";
        }
        else if(url.toString().contains(".pdf"))
        {
            // PDF file
            dataType = "application/pdf";
        }
        else if(url.toString().contains(".ppt") || url.toString().contains(".pptx"))
        {
            // Powerpoint file
            dataType =  "application/vnd.ms-powerpoint";
        }
        else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            dataType = "application/vnd.ms-excel";
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar"))
        {
            // WAV audio file
            dataType = "application/x-wav";
        }
        else if(url.toString().contains(".jpg"))
        {
            // WAV audio file
            dataType = "image/jpg";
        }
        else if(url.toString().contains(".png"))
        {
            // WAV audio file
            dataType = "image/png";
        }
        openDocument(url, dataType);
    }

    private void openDocument(String path,String dataType)
    {
        File file = new File(path);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            uri = FileProvider.getUriForFile(Previously_watched.this, "com.bia.bodhi.provider", file);
            Log.e("urihere", String.valueOf(uri));
        } else
        {
            uri = Uri.fromFile(file);
            Log.e("urihere", String.valueOf(uri));
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(uri, dataType);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            Previously_watched.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Previously_watched.this, "Application not found", Toast.LENGTH_SHORT).show();
        }

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String>
    {
        String extension = "";
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //  showDialog(progress_bar_type);
            //ShowDialog();
            dialog= new ProgressDialog(Previously_watched.this);
            dialog.setMessage("Fetching...");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();

        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try
            {


                if (f_url[0].contains("."))
                    extension = f_url[0].substring(f_url[0].lastIndexOf("."));

                URL url = new URL(f_url[0]);
                Log.e("LOG KA",f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/file"+extension);


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //       publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage

            dialog.setMessage("Fetching ..." + progress[0]+ "%");

            Log.e("Progress - ", String.valueOf(Integer.parseInt(progress[0])));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url)
        {



            // dismiss the dialog after the file was downloaded
            Log.e("hoighj","yaha dekho");
            //String x = Commons.getPath(Uri.parse(Environment.getExternalStorageDirectory().toString()+ "/doc.docx"), context);
            String x = Environment
                    .getExternalStorageDirectory().toString()
                    + "/file"+extension;
            CheckFile(x);
            dialog.dismiss();
            dialog.cancel();
        }

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
