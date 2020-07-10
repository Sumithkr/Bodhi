package com.bia.bodhinew.School;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class NoticeFragment extends Fragment {

    String[] NoticeContent = new String[1000];
    String[] NoticeUrl = new String[1000];
    String[] NoticeClass = new String[1000];
    String[] NoticeDateTime = new String[1000];
    String[] Class_list = {"Class", "1", "2", "3", "4", "5","6","7","8","9","10","11","12" };
    String[] Days_list = {"Days","30", "60", "90", "120"};
    Spinner spin_class,spin_days;
    static ArrayList<Modelclass> listing_of_notice;
    String Cls, Day;
    int ClassInt;
    ListView notice_list;
    private static SchoolNoticeShowAdaptor adaptor;
    ImageView nodata;
    Button CreateNotice_Button;
    View v;
    ACProgressPie dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notice, container, false);

        nodata = (ImageView)v.findViewById(R.id.nodata);
        notice_list = v.findViewById(R.id.notice_list);
        //StartServerFile();
       //Class spinner
        spin_class = (Spinner)v. findViewById(R.id.NoticeFragment_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_class.setAdapter(adapter);
        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0)
                {
                    Cls = Class_list[position];
                    Arrays.fill(NoticeContent, null);
                    Arrays.fill(NoticeDateTime, null);
                    Arrays.fill(NoticeClass, null);
                    Arrays.fill(NoticeUrl, null);
                    StartServerFile();
                }
                else {
                    // All data to be shown without any condition
                    Cls = "All";

                    if(ClassInt == 1) {

                        Arrays.fill(NoticeContent, null);
                        Arrays.fill(NoticeDateTime, null);
                        Arrays.fill(NoticeClass, null);
                        Arrays.fill(NoticeUrl, null);
                        StartServerFile();

                    }
                }
                //GetPublisherResults(Cls);
                Log.e("class", Cls);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner days
        spin_days = (Spinner)v. findViewById(R.id.NoticeFragment_Month);
        ArrayAdapter<String> adaptor_days = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Days_list);
        adaptor_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_days.setAdapter(adaptor_days);
        spin_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Day = "";

                if(position != 0) {

                    Day = Days_list[position];
                    Arrays.fill(NoticeContent, null);
                    Arrays.fill(NoticeDateTime, null);
                    Arrays.fill(NoticeClass, null);
                    Arrays.fill(NoticeUrl, null);
                    StartServerFile();
                }

                else {

                    ClassInt++;
                    Arrays.fill(NoticeContent, null);
                    Arrays.fill(NoticeDateTime, null);
                    Arrays.fill(NoticeClass, null);
                    Arrays.fill(NoticeUrl, null);
                    StartServerFile();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CreateNotice_Button = (Button)v.findViewById(R.id.CreateNotice_Button);
        CreateNotice_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Notice_post.class);
                startActivity(intent);
            }
        });

        return v;
    }


    private ArrayList<Modelclass> GetPublisherResults(String Cls) {

        ArrayList<Modelclass> results = new ArrayList<>();
        Log.e("cls",Cls);
        int i = 0;

        while (NoticeClass[i] != null)
        {
            //boolean checkdata = true;
            Modelclass notice = new Modelclass();
            if(Cls.equals("All"))
            {
                //Log.e("checkone",NoticeClass[i]);
                //checkdata = false;
                nodata.setVisibility(View.GONE);
                notice_list.setVisibility(View.VISIBLE);
                notice.setContent_of_notice(NoticeContent[i]);
                notice.setDatetime_of_notice(NoticeDateTime[i]);
                notice.setImg_of_notice(NoticeUrl[i]);

                if (NoticeUrl[i].equals("") || NoticeUrl[i] == null)

                    notice.setBoolImage(false);

                else {

                    if (NoticeUrl[i].contains(".jpg") || NoticeUrl[i].contains(".png") || NoticeUrl[i].contains(".jpeg") || NoticeUrl[i].contains(".docx") || NoticeUrl[i].contains(".pdf"))

                        notice.setBoolImage(true);

                }

                results.add(notice);
            }

            else {

                if (NoticeClass[i].equals(Cls))

                {
                    Log.e("checktwo",Cls);
                    nodata.setVisibility(View.GONE);
                    notice_list.setVisibility(View.VISIBLE);
                    notice.setContent_of_notice(NoticeContent[i]);
                    //Log.e("Notice Content", NoticeContent[i]+"--------------------------------------------------------");
                    notice.setDatetime_of_notice(NoticeDateTime[i]);
                    notice.setImg_of_notice(NoticeUrl[i]);

                    if (NoticeUrl[i].equals("") || NoticeUrl[i] == null) {
                        notice.setBoolImage(false);

                    } else {
                        if (NoticeUrl[i].contains(".jpg") || NoticeUrl[i].contains(".png") || NoticeUrl[i].contains(".jpeg")
                                || NoticeUrl[i].contains(".docx") || NoticeUrl[i].contains(".pdf"))
                            notice.setBoolImage(true);
                    }

                    results.add(notice);
                }
            }
            /*if (checkdata)
            {
                Log.e("true or false","checkthree");
                //nodata.setVisibility(View.VISIBLE);
                //notice_list.setVisibility(View.GONE);
            }*/
            i++;
        }
        return results;
    }


    public void onPreServerFile()
    {
        /*dialog=new ProgressDialog(getContext());
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);*/

        dialog = new ACProgressPie.Builder(getActivity())
                .ringColor(Color.parseColor("#fa3a0f"))
                .pieColor(Color.parseColor("#fa3a0f"))
                .bgAlpha(1)
                .bgColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.show();
    }
    public void StartServerFile()
    {
        onPreServerFile();
        String url = "https://bodhi.shwetaaromatics.co.in/School/FetchNotice.php?UserID="+file_retreive()+"&Day="+Day;
        Log.e("Second Step", url+"--------------------------------------------------------");


        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                try
                {

                    ConvertFromJSON(output);
                    listing_of_notice = GetPublisherResults(Cls);
                    adaptor= new SchoolNoticeShowAdaptor(getContext(), listing_of_notice);
                    notice_list.setAdapter(adaptor);

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
                try {
                    NoticeContent[i] = URLDecoder.decode(obj.getString("NoticeText"), "UTF-8");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                //NoticeContent[i] = obj.getString("NoticeText");
                NoticeUrl[i] = obj.getString("NoticeURL");
                Log.e("Forth Step", NoticeUrl[i]+"--------------------------------------------------------");
                NoticeClass[i] = obj.getString("Class");
                Log.e("noticeclass",NoticeClass[i]);
                NoticeDateTime[i] = obj.getString("DateTime");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dialog.dismiss();
        dialog.cancel();
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
}
