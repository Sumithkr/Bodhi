package com.bia.bodhinew.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bia.bodhinew.R;
import com.bia.bodhinew.School.LoginActivitySchool;
import com.bia.bodhinew.School.Master_activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomePage extends Fragment {

    ArrayList<HomeDetailsGetandSetVideos> homeClassVideos;
    ArrayList<HomeDetailsGetandSetBooks> homeClassBooks;
    ArrayList<HomeDetailsGetandSetRevisionArticle> homeClassRevisionArticles;
    ArrayList<HomeDetailsGetandSetRevisionMedia> homeClassRevisionMedia;
    ArrayList<HomeDetailsGetandSetSubjects> homeClassSubjects;
    String[] UploadID = new String[1000];
    String[] Name= new String[1000];
    String[] DateTime= new String[1000];
    String[] Description = new String[1000];
    String[] FileURL= new String[1000];
    String[] ThumbnailURL = new String[1000];
    String[] SubjectID= new String[1000];
    String[] SubjectName = new String[1000];
    String[] Type= new String[1000];
    String[] isPublic= new String[1000];
    String[] StudentClass= new String[1000];
    ProgressDialog dialog1;
    Button Previously_watched_button;
    private boolean firstTime = true;
    int universal=0, TotalVideoint= 0, TotalBooksint =0, TotalMediaAttachmentsint =0 , TotalArticlesint =0, SubjectContain=0;
    TextView TotalVideos, TotalBooks, TotalMediaAttachments, TotalArticles;
    ArrayList<HomeDetailsGetandSetVideos> resultsVideos = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetBooks> resultsBooks = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetRevisionArticle> resultsRevisionArticle = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetRevisionMedia> resultsRevisonMedia = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetSubjects> resultsSubjects = new ArrayList<>();
    ArrayList<String> resultSubjectCopy= new ArrayList<>();


    View RootView;

    /*public void  checkPermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener()
                {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        if(firstTime)
                        {
                            Log.e("permission", "granted");
                            //new UploadFeed().execute();
                            firstTime = false;
                        }

//                        Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                        //    new UploadFeed().execute();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {
                        // check for permanent denial of permission
                        Log.e("permission","denied");
//                      Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                        new HomePage().finish();
                        moveTaskToBack(false);
                        firstTime = true;

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        /*if (response.isPermanentlyDenied()) {
                            // navigate user to app settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.activity_home_page, container, false);

        TotalVideos= RootView.findViewById(R.id.total_videos);
        TotalBooks = RootView.findViewById(R.id.total_book);
        TotalArticles = RootView.findViewById(R.id.total_articles);
        TotalMediaAttachments= RootView.findViewById(R.id.total_media_attachements);
        Previously_watched_button = (Button)RootView.findViewById(R.id.Previously_watched_button);
        Previously_watched_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent success = new Intent(getActivity(), Previously_watched.class);
                startActivity(success);
            }
        });

        StartServerFile();

        return RootView;

    }

    public void StartServerFile()
    {
        loading();

        String url = "http://bodhi.shwetaaromatics.co.in/Student/FetchHomeMedia.php?UserID="+file_retreive();

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {

                try
                {
                    ConvertFromJSON(output);
                    PostExecute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    public void PostExecute()
    {
        while(Type[universal]!= null){

            if(Type[universal].equals("0")) {

                homeClassBooks = GetBooksDetailing();
                TotalBooksint++;

            }

            if(Type[universal].equals("1")) {

                homeClassVideos = GetVideoDetailing();
                TotalVideoint++;

            }

            if(Type[universal].equals("2")) {

                if( FileURL[universal].contains(".mp4") || FileURL[universal].contains(".3gp")
                        || FileURL[universal].contains(".jpg") ||FileURL[universal].contains(".png")) {

                    homeClassRevisionMedia = GetRevisionMediaDetailing();
                    TotalMediaAttachmentsint++;

                }

                else {

                    homeClassRevisionArticles = GetRevisionArticleDetailing();
                    TotalArticlesint++;

                }

            }

            homeClassSubjects = GetSubjectDetailing();

            universal++;
        }

        SettingTextViews();
        initRecyclerViewVideos();
        initRecyclerViewBooks();
        initRecyclerViewRevisionArticle();
        initRecyclerViewRevisionMedia();
        initRecyclerViewSubjects();

    }

    private void ConvertFromJSON(String json)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject obj_outer = null;
            obj_outer= jsonArray.getJSONObject(0);

            JSONArray m_JSON = obj_outer.getJSONArray("UploadData");


                for (int i = 0; i < m_JSON.length(); i++) {

                    JSONObject obj = m_JSON.getJSONObject(i);
                    UploadID[i] = obj.getString("ID");
                    Name[i] = obj.getString("Name");
                    DateTime[i] = obj.getString("DateTime");
                    Description[i] = obj.getString("Description");
                    FileURL[i] = obj.getString("URL");
                    ThumbnailURL[i] = obj.getString("ThumbnailURL");
                    SubjectID[i] = obj.getString("SubjectID");
                    SubjectName[i] = obj.getString("SubjectName");
                    /*if(Arrays.asList(SubjectName).contains(SubjectName[i])){

                        Log.e("SubjectName", SubjectName[i]);

                    }else {

                    }*/
                    Type[i] = obj.getString("Type");
                    isPublic[i] = obj.getString("isPublic");
                    //StudentClass[i] = obj.getString("Class");

                }

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void SettingTextViews(){

        TotalVideos.setText(TotalVideoint + " Video");
        TotalBooks.setText(TotalBooksint + " Book");
        TotalArticles.setText(TotalArticlesint + " Article");
        TotalMediaAttachments.setText(TotalMediaAttachmentsint + " Media Attachment");

    }

    private void initRecyclerViewVideos(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForVideos adapter = new HomePageRecyclerAdapterForVideos(getContext(), homeClassVideos);
        recyclerView.setAdapter(adapter);

    }

    private void initRecyclerViewBooks(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForBooks adapter = new HomePageRecyclerAdapterForBooks(getContext(), homeClassBooks);
        recyclerView.setAdapter(adapter);

    }

    private void initRecyclerViewRevisionArticle(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionArticles);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForRevisionArticle adapter = new HomePageRecyclerAdapterForRevisionArticle(getContext(), homeClassRevisionArticles);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewRevisionMedia(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionMedia);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForRevisionMedia adapter = new HomePageRecyclerAdapterForRevisionMedia(getContext(), homeClassRevisionMedia);
        recyclerView.setAdapter(adapter);
        dialog1.dismiss();
        dialog1.cancel();
    }

    private void initRecyclerViewSubjects(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewSubject);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForSubjects adapter = new HomePageRecyclerAdapterForSubjects(getContext(), homeClassSubjects);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<HomeDetailsGetandSetVideos> GetVideoDetailing()
    {

        HomeDetailsGetandSetVideos home = new HomeDetailsGetandSetVideos();
        home.setName(Name[universal]);
        Log.e("Description", Description[universal]);
        home.setThumbnailURL(ThumbnailURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);

        resultsVideos.add(home);

        return resultsVideos;
    }

    private ArrayList<HomeDetailsGetandSetBooks> GetBooksDetailing()
    {

        HomeDetailsGetandSetBooks home = new HomeDetailsGetandSetBooks();
        home.setName(Name[universal]);
        Log.e("Description", Description[universal]);
        home.setThumbnailURL(FileURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);

        resultsBooks.add(home);

        return resultsBooks;
    }

    private ArrayList<HomeDetailsGetandSetRevisionArticle> GetRevisionArticleDetailing()
    {

        HomeDetailsGetandSetRevisionArticle home = new HomeDetailsGetandSetRevisionArticle();
        home.setName(Name[universal]);
        home.setThumbnailURL(FileURL[universal]);
        //Log.e("Description", FileURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);

        resultsRevisionArticle.add(home);

        return resultsRevisionArticle;
    }

    private ArrayList<HomeDetailsGetandSetRevisionMedia> GetRevisionMediaDetailing()
    {

        HomeDetailsGetandSetRevisionMedia home = new HomeDetailsGetandSetRevisionMedia();
        home.setName(Name[universal]);
        Log.e("Description", Description[universal]);

        if(ThumbnailURL[universal].equals("") || ThumbnailURL[universal].equals(null)){

            home.setThumbnailURL(FileURL[universal]);

        }
        else {

            home.setThumbnailURL(ThumbnailURL[universal]);

        }

        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);

        resultsRevisonMedia.add(home);

        return resultsRevisonMedia;
    }

    private ArrayList<HomeDetailsGetandSetSubjects> GetSubjectDetailing()
    {

        HomeDetailsGetandSetSubjects home = new HomeDetailsGetandSetSubjects();
        home.setSubjectName(SubjectName[universal]);
        boolean present = resultSubjectCopy.contains(SubjectName[universal]);
        Log.e("SubjectName", String.valueOf(present));

        if(SubjectContain == 0){

            resultsSubjects.add(home);
            resultSubjectCopy.add(SubjectName[universal]);
            present= true;
            SubjectContain++;

        }

        if(!present) {

            resultsSubjects.add(home);
            resultSubjectCopy.add(SubjectName[universal]);
        }

        return resultsSubjects;
    }

    public void loading()
    {
        dialog1=new ProgressDialog(getContext());
        dialog1.setMessage("Please wait..");
        dialog1.setCancelable(false);
        dialog1.setInverseBackgroundForced(false);
        dialog1.show();
    }

    private String file_retreive()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = getContext().openFileInput("Bodhi_Login");
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
