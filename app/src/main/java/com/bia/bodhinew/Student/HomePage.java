package com.bia.bodhinew.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bia.bodhinew.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class HomePage extends Fragment {

    static ArrayList<HomeDetailsGetandSetVideos> homeClassVideos;
    static ArrayList<HomeDetailsGetandSetBooks> homeClassBooks;
    static ArrayList<HomeDetailsGetandSetRevisionArticle> homeClassRevisionArticles;
    static ArrayList<HomeDetailsGetandSetRevisionMedia> homeClassRevisionMedia;
    static ArrayList<HomeDetailsGetandSetSubjects> homeClassSubjects;
    static String[] UploadID = new String[1000];
    static String[] Name= new String[1000];
    static String[] DateTime= new String[1000];
    static String[] Description = new String[1000];
    static String[] FileURL= new String[1000];
    static String[] ThumbnailURL = new String[1000];
    static String[] SubjectID= new String[1000];
    static String[] SubjectName = new String[1000];
    static String[] Type= new String[1000];
    static String[] isPublic= new String[1000];
    static String[] StudentClass= new String[1000];
    ACProgressPie dialog;
    CardView Previously_watched_button;
    private boolean firstTime = true;
    static int universal=0, TotalVideoint= 0, TotalSubjectsint= 0, TotalBooksint =0, TotalMediaAttachmentsint =0 , TotalArticlesint =0, SubjectContain=0;
    static TextView TotalVideos, TotalBooks, TotalSubjects, TotalMediaAttachments, TotalArticles, SchoolName, StudentName;
    static ArrayList<HomeDetailsGetandSetVideos> resultsVideos = new ArrayList<>();
    static ArrayList<HomeDetailsGetandSetBooks> resultsBooks = new ArrayList<>();
    static ArrayList<HomeDetailsGetandSetRevisionArticle> resultsRevisionArticle = new ArrayList<>();
    static ArrayList<HomeDetailsGetandSetRevisionMedia> resultsRevisonMedia = new ArrayList<>();
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

        RootView = inflater.inflate(R.layout.activity_home_page, container, false);

        TotalVideos= RootView.findViewById(R.id.total_videos);
        TotalBooks = RootView.findViewById(R.id.total_book);
        TotalArticles = RootView.findViewById(R.id.total_articles);
        TotalMediaAttachments= RootView.findViewById(R.id.total_media_attachements);
        SchoolName= RootView.findViewById(R.id.SchoolName);
        StudentName= RootView.findViewById(R.id.StudentName);
        TotalSubjects= RootView.findViewById(R.id.total_subject);
        StudentName.setText(file_retreive_StudentName());
        SchoolName.setText(file_retreive_school());
        MasterStudentActivity.currentFragment= "Master_Activity";
        Previously_watched_button = (CardView) RootView.findViewById(R.id.Previously_watched_button);
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

        String url = "https://bodhi.shwetaaromatics.co.in/Student/FetchHomeMedia.php?UserID="+file_retreive();

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
        initRecyclerViewVideos("0");
        initRecyclerViewBooks("0");
        initRecyclerViewRevisionArticle("0");
        initRecyclerViewRevisionMedia("0");
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
        TotalSubjects.setText(TotalSubjectsint + " Subjects");

    }

    private void initRecyclerViewVideos(String MediaID){

        if(!MediaID.equals("0"))
        {
            homeClassVideos.clear();
            SetNewDatInVideoAdapter(MediaID);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForVideos adapter = new HomePageRecyclerAdapterForVideos(getContext(), homeClassVideos);
        recyclerView.setAdapter(adapter);

    }

    public static void SetNewDatInVideoAdapter(String MediaID)
    {
        universal = 0;
        TotalVideoint = 0;
        while(Type[universal]!= null)
        {
            if(Type[universal].equals("1"))
            {
                if(!UploadID[universal].equals(MediaID) && !UploadID[universal].equals("null"))
                {
                    homeClassVideos = GetVideoDetailing();
                    TotalVideoint++;
                }
                else
                {
                    UploadID[universal] = "null";
                }
            }
            universal++;
        }

        Log.e("Total Videos", String.valueOf(TotalVideoint));
        TotalVideos.setText(TotalVideoint+" Videos");
    }

    private void initRecyclerViewBooks(String MediaID){

        if(!MediaID.equals("0"))
        {
            homeClassBooks.clear();
            SetNewDatInBookAdapter(MediaID);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForBooks adapter = new HomePageRecyclerAdapterForBooks(getContext(), homeClassBooks);
        recyclerView.setAdapter(adapter);

    }

    public static void SetNewDatInBookAdapter(String MediaID)
    {
        universal = 0;
        TotalBooksint = 0;
        while(Type[universal]!= null)
        {
            if(Type[universal].equals("0"))
            {
                if(!UploadID[universal].equals(MediaID) && !UploadID[universal].equals("null"))
                {
                    homeClassBooks = GetBooksDetailing();
                    TotalBooksint++;
                }
                else
                {
                    UploadID[universal] = "null";
                }
            }
            universal++;
        }

        TotalBooks.setText(TotalBooksint+" Books");
    }

    private void initRecyclerViewRevisionArticle(String MediaID){

        if(!MediaID.equals("0"))
        {
            homeClassRevisionArticles.clear();
            SetNewDatInRevisionArticleAdapter(MediaID);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionArticles);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForRevisionArticle adapter = new HomePageRecyclerAdapterForRevisionArticle(getContext(), homeClassRevisionArticles);
        recyclerView.setAdapter(adapter);
    }

    public static void SetNewDatInRevisionArticleAdapter(String MediaID)
    {
        universal = 0;
        TotalArticlesint = 0;
        while(Type[universal]!= null)
        {
            if(Type[universal].equals("2")) {
                if (FileURL[universal].contains(".mp4") || FileURL[universal].contains(".3gp")
                        || FileURL[universal].contains(".jpg") || FileURL[universal].contains(".png")) {
                } else {
                    if (!UploadID[universal].equals(MediaID) && !UploadID[universal].equals("null")) {
                        homeClassRevisionArticles = GetRevisionArticleDetailing();
                        TotalArticlesint++;
                    } else {
                        UploadID[universal] = "null";
                    }
                }
            }
            universal++;
        }
        TotalArticles.setText(TotalArticlesint + " Article");
    }

    private void initRecyclerViewRevisionMedia(String MediaID){

        if(!MediaID.equals("0"))
        {
            homeClassRevisionMedia.clear();
            SetNewDatInRevisionMediaAdapter(MediaID);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionMedia);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForRevisionMedia adapter = new HomePageRecyclerAdapterForRevisionMedia(getContext(), homeClassRevisionMedia);
        recyclerView.setAdapter(adapter);
    }

    public static void SetNewDatInRevisionMediaAdapter(String MediaID)
    {
        universal = 0;
        TotalMediaAttachmentsint = 0;
        while(Type[universal]!= null)
        {
            if(Type[universal].equals("2")) {
                if (FileURL[universal].contains(".mp4") || FileURL[universal].contains(".3gp")
                        || FileURL[universal].contains(".jpg") || FileURL[universal].contains(".png")) {

                    if (!UploadID[universal].equals(MediaID) && !UploadID[universal].equals("null")) {
                        homeClassRevisionMedia = GetRevisionMediaDetailing();
                        TotalMediaAttachmentsint++;
                    } else {
                        UploadID[universal] = "null";
                    }
                }

            }
            universal++;
        }
        TotalMediaAttachments.setText(TotalMediaAttachmentsint+" Media Attachments");
    }

    private void initRecyclerViewSubjects(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewSubject);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForSubjects adapter = new HomePageRecyclerAdapterForSubjects(getContext(), homeClassSubjects);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();
        dialog.cancel();

    }

    private static ArrayList<HomeDetailsGetandSetVideos> GetVideoDetailing()
    {

        HomeDetailsGetandSetVideos home = new HomeDetailsGetandSetVideos();
        home.setName(Name[universal]);
        Log.e("Description", Description[universal]);
        home.setThumbnailURL(ThumbnailURL[universal]);
        home.setDescription(Description[universal]);
        home.setUploadID(UploadID[universal]);
        home.setSubjectName(SubjectName[universal]);
        home.setURL(FileURL[universal]);

        resultsVideos.add(home);

        return resultsVideos;
    }

    private static ArrayList<HomeDetailsGetandSetBooks> GetBooksDetailing()
    {

        HomeDetailsGetandSetBooks home = new HomeDetailsGetandSetBooks();
        home.setName(Name[universal]);
        //Log.e("Description", UploadID[universal]);
        home.setThumbnailURL(FileURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);
        home.setUploadID(UploadID[universal]);
        home.setURL(FileURL[universal]);

        resultsBooks.add(home);

        return resultsBooks;
    }

    private static ArrayList<HomeDetailsGetandSetRevisionArticle> GetRevisionArticleDetailing()
    {

        HomeDetailsGetandSetRevisionArticle home = new HomeDetailsGetandSetRevisionArticle();
        home.setName(Name[universal]);
        home.setThumbnailURL(FileURL[universal]);
        //Log.e("Upload ID", UploadID[universal]);
        home.setDescription(Description[universal]);
        home.setUploadID(UploadID[universal]);
        home.setSubjectName(SubjectName[universal]);

        resultsRevisionArticle.add(home);

        return resultsRevisionArticle;
    }

    private static ArrayList<HomeDetailsGetandSetRevisionMedia> GetRevisionMediaDetailing()
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
        home.setUploadID(UploadID[universal]);
        home.setURL(FileURL[universal]);

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
            TotalSubjectsint++;

        }

        if(!present) {

            resultsSubjects.add(home);
            resultSubjectCopy.add(SubjectName[universal]);
            TotalSubjectsint++;
        }

        return resultsSubjects;
    }

    public void loading()
    {
        dialog = new ACProgressPie.Builder(getActivity())
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

    private String file_retreive_school()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = getContext().openFileInput("Bodhi_School");
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

    private String file_retreive_StudentName()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = getContext().openFileInput("Bodhi_StudentName");
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
