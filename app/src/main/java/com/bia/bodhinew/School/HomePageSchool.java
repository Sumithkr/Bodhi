package com.bia.bodhinew.School;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HomePageSchool extends Fragment {

    static ArrayList<HomeDetailsGetandSetVideosSchool> homeClassVideos;
    static ArrayList<HomeDetailsGetandSetBooksSchool> homeClassBooks;
    static ArrayList<HomeDetailsGetandSetRevisionArticleSchool> homeClassRevisionArticles;
    static ArrayList<HomeDetailsGetandSetRevisionMediaSchool> homeClassRevisionMedia;
    static ArrayList<HomeDetailsGetandSetSubjectsSchool> homeClassSubjects;
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
    String[] StudentClass= new String[1000];
    ProgressDialog dialog1;
    private boolean firstTime = true;
    static int universal=0;
    static int TotalVideoint= 0;
    static int TotalBooksint =0;
    static int TotalMediaAttachmentsint =0;
    static int TotalArticlesint =0;
    int SubjectContain=0;
    static TextView TotalVideos, TotalBooks, TotalMediaAttachments, TotalArticles, SchoolName;
    static ArrayList<HomeDetailsGetandSetVideosSchool> resultsVideos = new ArrayList<>();
    static ArrayList<HomeDetailsGetandSetBooksSchool> resultsBooks = new ArrayList<>();
    static ArrayList<HomeDetailsGetandSetRevisionArticleSchool> resultsRevisionArticle = new ArrayList<>();
    static ArrayList<HomeDetailsGetandSetRevisionMediaSchool> resultsRevisonMedia = new ArrayList<>();
    ArrayList<HomeDetailsGetandSetSubjectsSchool> resultsSubjects = new ArrayList<>();
    ArrayList<String> resultSubjectCopy= new ArrayList<>();


    static View RootView;
    static Context c;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.activity_home_page_school, container, false);

        TotalVideos= RootView.findViewById(R.id.total_videos);
        TotalBooks = RootView.findViewById(R.id.total_book);
        TotalArticles = RootView.findViewById(R.id.total_articles);
        TotalMediaAttachments= RootView.findViewById(R.id.total_media_attachements);
        SchoolName= RootView.findViewById(R.id.SchoolName);
        SchoolName.setText(file_retreive_school());
        c = getActivity();
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

    public static void initRecyclerViewVideos(String MediaID){

        if(!MediaID.equals("0"))
        {
            homeClassVideos.clear();
            SetNewDatInVideoAdapter(MediaID);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForVideosSchool adapter = new HomePageRecyclerAdapterForVideosSchool(c, homeClassVideos);
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

    public static void initRecyclerViewBooks(String MediaID)
    {
        if(!MediaID.equals("0"))
        {
            homeClassBooks.clear();
            SetNewDatInBookAdapter(MediaID);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForBooksSchool adapter = new HomePageRecyclerAdapterForBooksSchool(c, homeClassBooks);
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



    public static void initRecyclerViewRevisionArticle(String MediaID)
    {

        if(!MediaID.equals("0"))
        {
            homeClassRevisionArticles.clear();
            SetNewDatInRevisionArticleAdapter(MediaID);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionArticles);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForRevisionArticleSchool adapter = new HomePageRecyclerAdapterForRevisionArticleSchool(c, homeClassRevisionArticles);
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
    public static void initRecyclerViewRevisionMedia(String MediaID){

        if(!MediaID.equals("0"))
        {
            homeClassRevisionMedia.clear();
            SetNewDatInRevisionMediaAdapter(MediaID);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerViewRevisionMedia);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapterForRevisionMediaSchool adapter = new HomePageRecyclerAdapterForRevisionMediaSchool(c, homeClassRevisionMedia);
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
        HomePageRecyclerAdapterForSubjectsSchool adapter = new HomePageRecyclerAdapterForSubjectsSchool(getContext(), homeClassSubjects);
        recyclerView.setAdapter(adapter);
        dialog1.dismiss();
        dialog1.cancel();

    }

    private static ArrayList<HomeDetailsGetandSetVideosSchool> GetVideoDetailing()
    {

        HomeDetailsGetandSetVideosSchool home = new HomeDetailsGetandSetVideosSchool();
        home.setName(Name[universal]);
        Log.e("School Name", Name[universal]);
        home.setThumbnailURL(ThumbnailURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);
        home.setUploadID(UploadID[universal]);
        home.setURL(FileURL[universal]);

        resultsVideos.add(home);

        return resultsVideos;
    }

    private static ArrayList<HomeDetailsGetandSetBooksSchool> GetBooksDetailing()
    {

        HomeDetailsGetandSetBooksSchool home = new HomeDetailsGetandSetBooksSchool();
        home.setName(Name[universal]);
        home.setThumbnailURL(FileURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);
        home.setUploadID(UploadID[universal]);
        home.setURL(FileURL[universal]);

        resultsBooks.add(home);

        return resultsBooks;
    }

    private static ArrayList<HomeDetailsGetandSetRevisionArticleSchool> GetRevisionArticleDetailing()
    {

        HomeDetailsGetandSetRevisionArticleSchool home = new HomeDetailsGetandSetRevisionArticleSchool();
        home.setName(Name[universal]);
        home.setThumbnailURL(FileURL[universal]);
        //Log.e("Description", FileURL[universal]);
        home.setDescription(Description[universal]);
        home.setSubjectName(SubjectName[universal]);
        home.setUploadID(UploadID[universal]);

        resultsRevisionArticle.add(home);

        return resultsRevisionArticle;
    }

    private static ArrayList<HomeDetailsGetandSetRevisionMediaSchool> GetRevisionMediaDetailing()
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
        home.setUploadID(UploadID[universal]);
        home.setURL(FileURL[universal]);

        resultsRevisonMedia.add(home);

        return resultsRevisonMedia;
    }

    private ArrayList<HomeDetailsGetandSetSubjectsSchool> GetSubjectDetailing()
    {

        HomeDetailsGetandSetSubjectsSchool home = new HomeDetailsGetandSetSubjectsSchool();
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

}
