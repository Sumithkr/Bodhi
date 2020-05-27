package com.bia.bodhinew.Student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import com.bia.bodhinew.R;
import com.bia.bodhinew.School.Modelclass;

import androidx.fragment.app.Fragment;


public class StudentNoticePage extends Fragment {
    String[] NoticeContent = new String[1000];
    String[] NoticeUrl = new String[1000];
    String[] NoticeClass = new String[1000];
    String[] NoticeDateTime = new String[1000];
    String[] Days_list = {"Select Days","30", "60", "90", "120"};
    Spinner spin_days;
    String Day;
    ListView notice_list;
    ImageView nodata;
    String[] msg_read = new String[1000];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.student_notice_layout, container, false);
        nodata = (ImageView)v.findViewById(R.id.nodata);
        notice_list = v.findViewById(R.id.notice_list);
        //Class Months
        spin_days = (Spinner)v. findViewById(R.id.StudentNoticePage_Months);
        ArrayAdapter<String> adaptor_days = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Days_list);
        adaptor_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_days.setAdapter(adaptor_days);
        spin_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Day = "";
                if(position != 0) {
                    Day = Days_list[position];
                    Log.e("day", Day);
                    StartServerFile();
                }
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
        if(NoticeContent[i] == null)
        {
            nodata.setVisibility(View.VISIBLE);
            notice_list.setVisibility(View.GONE);
        }
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
            if(msg_read[i].equals("1"))
            {
                notice.setBoolMSgRead(true);
            }
            else
            {
                notice.setBoolMSgRead(false);
            }
            i++;
            results.add(notice);
        }

        return results;
    }

    public void StartServerFile()
    {
        String url = "http://bodhi.shwetaaromatics.co.in/Student/FetchNotice.php?UserID="+file_retreive()+"&Day="+Day;
        Log.e("TAG",url);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.CUPCAKE) {
            FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
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
                NoticeDateTime[i] = obj.getString("DateTime");
                msg_read[i] = obj.getString("NoticeID");
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
