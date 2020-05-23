package com.bia.bodhi.Student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.bia.bodhi.FetchFromDB;
import com.bia.bodhi.R;
import com.bia.bodhi.School.Modelclass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class StudentNoticePage extends Fragment {
    String[] NoticeContent = new String[1000];
    String[] NoticeUrl = new String[1000];
    String[] NoticeClass = new String[1000];
    String[] NoticeDateTime = new String[1000];
    String[] Class_list = { "1st", "2nd", "3rd", "4th", "5th","6th","7th","8th","9th","10th","11th","12th" };
    Spinner spin_class;
    String Cls;
    ListView notice_list;
    String[] msg_read = new String[1000];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.student_notice_layout, container, false);
        StartServerFile();
        notice_list = v.findViewById(R.id.notice_list);
        //Class spinner
        spin_class = (Spinner)v. findViewById(R.id.StudentNoticePage_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_class.setAdapter(adapter);
        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        return v;
    }

    private ArrayList<Modelclass> GetPublisherResults() {
        ArrayList<Modelclass> results = new ArrayList<>();

        int i =0;
        while (NoticeContent[i] != null)
        {
            Modelclass notice = new Modelclass();
            notice.setContent_of_notice(NoticeContent[i]);
            notice.setDatetime_of_notice(NoticeDateTime[i]);
            notice.setImg_of_notice(NoticeUrl[i]);
            if(NoticeUrl[i].equals("") || NoticeUrl[i] == null)
            {
                notice.setBoolImage(false);
            }
            else
            {
                if(NoticeUrl[i].contains(".jpg") || NoticeUrl[i].contains(".png") || NoticeUrl[i].contains(".jpeg")
                        || NoticeUrl[i].contains(".docx") || NoticeUrl[i].contains(".pdf") )
                    notice.setBoolImage(true);
            }
           /* if(msg_read[i].equals("1"))
            {
                notice.setBoolMSgRead(true);
            }
            else
            {
                notice.setBoolMSgRead(false);
            }*/

            i++;
            results.add(notice);
        }
        return results;
    }

    public void StartServerFile()
    {
        String url = "https://bodhi.shwetaaromatics.co.in/School/FetchNotice.php?UserID=2"+"&Day=60";
        Log.e("TAG",url);

        com.bia.bodhi.FetchFromDB asyncTask = (com.bia.bodhi.FetchFromDB) new com.bia.bodhi.FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                try
                {

                    ConvertFromJSON(output);

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
                Log.e("notice img url", NoticeUrl[i]);
                NoticeClass[i] = obj.getString("Class");
                Cls = NoticeClass[i];
                NoticeDateTime[i] = obj.getString("DateTime");
               // msg_read[i] = obj.getString("msg_read");
            }
            final ArrayList<Modelclass> listing_of_notice = GetPublisherResults();
            notice_list.setAdapter(new StudentNoticePageAdaptor(getActivity(), listing_of_notice));
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
            inputStream = getActivity().openFileInput("Bodhi_Login");
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
