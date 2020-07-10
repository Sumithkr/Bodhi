package com.bia.bodhinew.School;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bia.bodhinew.R;
import com.bia.bodhinew.Student.FetchFromDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectPage extends Fragment {

    ArrayList<HomeDetailsGetandSetVideosSchool> homeClassVideos;
    ArrayList<HomeDetailsGetandSetBooksSchool> homeClassBooks;
    ArrayList<HomeDetailsGetandSetRevisionArticleSchool> homeClassRevisionArticles;
    ArrayList<HomeDetailsGetandSetRevisionMediaSchool> homeClassRevisionMedia;
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
    LinearLayout NoVideosPanel, NoBooksPanel, NoRevisionArticlePanel, NoRevisionMediaPanel;
    private boolean firstTime = true;
    int universal=0, TotalVideoint= 0, TotalBooksint =0, TotalMediaAttachmentsint =0 , TotalArticlesint =0;
    TextView TotalVideos, TotalBooks, TotalMediaAttachments, TotalArticles, MainSubjectheading;
    ArrayList<HomeDetailsGetandSetVideosSchool> resultsVideos = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetBooksSchool> resultsBooks = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetRevisionArticleSchool> resultsRevisionArticle = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetRevisionMediaSchool> resultsRevisonMedia = new ArrayList<>();


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
                        new HomePageSchool().finish();
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

        RootView = inflater.inflate(R.layout.activity_subject_page, container, false);

        TotalVideos= RootView.findViewById(R.id.total_videos);
        TotalBooks = RootView.findViewById(R.id.total_book);
        TotalArticles = RootView.findViewById(R.id.total_articles);
        TotalMediaAttachments= RootView.findViewById(R.id.total_media_attachements);
        MainSubjectheading= RootView.findViewById(R.id.MainSubjectHeading);
        //Log.e("MainSubject", getArguments().getString("ForSubject"));
        MainSubjectheading.setText(getArguments().getString("ForSubject"));

        NoVideosPanel= RootView.findViewById(R.id.NoVideosPanel);
        NoBooksPanel= RootView.findViewById(R.id.NoBooksPanel);
        NoRevisionArticlePanel= RootView.findViewById(R.id.NoRevisionArticlePanel);
        NoRevisionMediaPanel= RootView.findViewById(R.id.NoRevisionMediaPanel);

        Master_activity.currentFragment= "SubjectPage";

        StartServerFile();

        return RootView;

    }

    public void StartServerFile()
    {
        loading();

        String url = "https://bodhi.shwetaaromatics.co.in/School/FetchHomeMedia.php?UserID="+file_retreive();

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

        while(SubjectName[universal]!= null){

            Log.e("Main Subject Heading", getArguments().getString("ForSubject"));

            if(SubjectName[universal].equals(getArguments().getString("ForSubject"))) {

                if (Type[universal].equals("0")) {

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

            }

            //homeClassSubjects = GetSubjectDetailing();

            universal++;
        }

        SettingTextViews();
        initRecyclerViewVideos();
        initRecyclerViewBooks();
        initRecyclerViewRevisionArticle();
        initRecyclerViewRevisionMedia();
        //initRecyclerViewSubjects();

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

        if(resultsVideos.isEmpty()){

            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerView);
            recyclerView.setVisibility(View.GONE);
            NoVideosPanel.setVisibility(View.VISIBLE);

        }

        else {

            NoVideosPanel.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(layoutManager);
            HomePageRecyclerAdapterForVideosSchool adapter = new HomePageRecyclerAdapterForVideosSchool(getContext(), homeClassVideos);
            recyclerView.setAdapter(adapter);

        }

    }

    private void initRecyclerViewBooks(){

        if(resultsBooks.isEmpty()){

            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewBooks);
            recyclerView.setVisibility(View.GONE);
            NoBooksPanel.setVisibility(View.VISIBLE);

        }

        else {

            NoBooksPanel.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewBooks);
            recyclerView.setLayoutManager(layoutManager);
            HomePageRecyclerAdapterForBooksSchool adapter = new HomePageRecyclerAdapterForBooksSchool(getContext(), homeClassBooks);
            recyclerView.setAdapter(adapter);
        }

    }

    private void initRecyclerViewRevisionArticle(){

        if(resultsRevisionArticle.isEmpty()){

            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionArticles);
            recyclerView.setVisibility(View.GONE);
            NoRevisionArticlePanel.setVisibility(View.VISIBLE);

        }

        else {

            NoRevisionArticlePanel.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionArticles);
            recyclerView.setLayoutManager(layoutManager);
            HomePageRecyclerAdapterForRevisionArticleSchool adapter = new HomePageRecyclerAdapterForRevisionArticleSchool(getContext(), homeClassRevisionArticles);
            recyclerView.setAdapter(adapter);
        }
    }

    private void initRecyclerViewRevisionMedia(){

        if(resultsRevisonMedia.isEmpty()){

            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionMedia);
            recyclerView.setVisibility(View.GONE);
            NoRevisionMediaPanel.setVisibility(View.VISIBLE);

        }

        else {

            NoRevisionMediaPanel.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionMedia);
            recyclerView.setLayoutManager(layoutManager);
            HomePageRecyclerAdapterForRevisionMediaSchool adapter = new HomePageRecyclerAdapterForRevisionMediaSchool(getContext(), homeClassRevisionMedia);
            recyclerView.setAdapter(adapter);
        }

        dialog1.dismiss();
        dialog1.cancel();
    }

    /*private void initRecyclerViewSubjects(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewSubject);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForSubjectsSchool adapter = new HomePageRecyclerAdapterForSubjectsSchool(getContext(), homeClassSubjects);
        recyclerView.setAdapter(adapter);

    }*/

    private ArrayList<HomeDetailsGetandSetVideosSchool> GetVideoDetailing()
    {

        HomeDetailsGetandSetVideosSchool home = new HomeDetailsGetandSetVideosSchool();
        home.setName(Name[universal]);
        Log.e("Description", Description[universal]);
        home.setThumbnailURL(ThumbnailURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);
        home.setURL(FileURL[universal]);

        resultsVideos.add(home);

        return resultsVideos;
    }

    private ArrayList<HomeDetailsGetandSetBooksSchool> GetBooksDetailing()
    {

        HomeDetailsGetandSetBooksSchool home = new HomeDetailsGetandSetBooksSchool();
        home.setName(Name[universal]);
        Log.e("Description", Description[universal]);
        home.setThumbnailURL(FileURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);

        resultsBooks.add(home);

        return resultsBooks;
    }

    private ArrayList<HomeDetailsGetandSetRevisionArticleSchool> GetRevisionArticleDetailing()
    {

        HomeDetailsGetandSetRevisionArticleSchool home = new HomeDetailsGetandSetRevisionArticleSchool();
        home.setName(Name[universal]);
        Log.e("Description", Description[universal]);
        home.setThumbnailURL(FileURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);

        resultsRevisionArticle.add(home);

        return resultsRevisionArticle;
    }

    private ArrayList<HomeDetailsGetandSetRevisionMediaSchool> GetRevisionMediaDetailing()
    {

        HomeDetailsGetandSetRevisionMediaSchool home = new HomeDetailsGetandSetRevisionMediaSchool();
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

    /*private ArrayList<HomeDetailsGetandSetSubjectsSchool> GetSubjectDetailing()
    {

        HomeDetailsGetandSetSubjectsSchool home = new HomeDetailsGetandSetSubjectsSchool();
        home.setSubjectName(SubjectName[universal]);

        resultsSubjects.add(home);

        return resultsSubjects;
    }*/

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
            inputStream = getContext().openFileInput("Bodhi_Login_School");
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
