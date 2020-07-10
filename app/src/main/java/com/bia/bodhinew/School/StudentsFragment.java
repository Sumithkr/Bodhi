package com.bia.bodhinew.School;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;

import androidx.fragment.app.Fragment;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;


public class StudentsFragment extends Fragment {
    String[] Class_list = { "Class","1", "2", "3", "4", "5","6","7","8","9","10","11","12" };
    String Cls;
    static ImageView nodata;
    static ListView list_students;
    static String[] StudentName = new String[1000];
    static String[] StudentID = new String[1000];
    //static String[] SearchedStudentID = new String[1000];
    String[] StudentEmail = new String[1000];
    String[] StudentDateTime = new String[1000];
    static String[] StudentisEnable = new String[1000];
    String data;
    private static ViewStudentShowAdaptor adaptor;
    static AutoCompleteTextView autoCompleteTextView;
    static ArrayList<Modelclass> list;
    static String[] hints= new String[100];
    static Context c;
    ACProgressPie dialog;
    TextView SchoolName;
    int ListSize=0;
    static final List<String> AllData = new ArrayList<String>();
    static final List<String> SearchedStudentID = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_students, container, false);
        SchoolName = v.findViewById(R.id.SchoolName);
        SchoolName.setText(file_retreive_school());
        nodata = (ImageView)v.findViewById(R.id.nodata);
        c = getActivity();
        // Class spinner
        Spinner spin = (Spinner)v. findViewById(R.id.StudentsFragment_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cls = "";
                if(position != 0)
                {
                    Cls = Class_list[position];
                    Log.e("class",Cls);
                    Arrays.fill(StudentName, null);
                    Arrays.fill(StudentID, null);
                    Arrays.fill(StudentEmail, null);
                    Arrays.fill(StudentDateTime, null);
                    Arrays.fill(StudentisEnable, null);
                    StartServerFile();
                }

                else

                    StartServerFile();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        list_students = (ListView)v.findViewById(R.id.School_StudentsFragment_list);
        autoCompleteTextView = (AutoCompleteTextView)v.findViewById(R.id.AutoCompleteTextView);
        data = autoCompleteTextView.getText().toString();
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                data = autoCompleteTextView.getText().toString();
                GetSearchedPublisher();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    public void onPreServerFile()
    {
        dialog = new ACProgressPie.Builder(getActivity())
                .ringColor(Color.parseColor("#fa3a0f"))
                .pieColor(Color.parseColor("#fa3a0f"))
                .bgAlpha(1)
                .bgColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.show();
        dialog.setCancelable(false);
    }

    public void StartServerFile()
    {
        onPreServerFile();
        String url = "https://bodhi.shwetaaromatics.co.in/School/FetchStudents.php?Class="+Cls+"&UserID="+file_retreive();
        Log.e("url",url);
        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                //this function executes after
                try
                {
                    ConvertFromJSON(output);
                    list = GetPublisherResults();
                    //list_students.setAdapter(new ViewStudentShowAdaptor(getActivity(), list));
                    adaptor = new ViewStudentShowAdaptor(getContext(), list);
                    list_students.setAdapter(adaptor);
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
                StudentName[i]=obj.getString("UserName");
                StudentID[i]=obj.getString("UserID");
                StudentEmail[i] = obj.getString("Email");
                StudentDateTime[i] = obj.getString("DateTime");
                StudentisEnable[i] = obj.getString("isEnable");

                if(StudentisEnable[i].equals("1")) {

                    ListSize++;
                    SearchedStudentID.add(obj.getString("UserID"));

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dialog.dismiss();
        dialog.cancel();
    }
    private static ArrayList<Modelclass> GetPublisherResults(String id)
    {
        ArrayList<Modelclass> results = new ArrayList<>();
        int k =0;
        if(StudentName[k] == null)
        {
            nodata.setVisibility(View.VISIBLE);
            list_students.setVisibility(View.GONE);
        }
        else {
            nodata.setVisibility(View.GONE);
            list_students.setVisibility(View.VISIBLE);
        }

        while (StudentName[k] != null)
        {

            Modelclass ar1 = new Modelclass();
            hints[k]=  StudentName[k];
            if(StudentisEnable[k].equals("1"))
            {
                if (id.equals(StudentID[k]) == false && !StudentID[k].equals("null"))
                {
                    ar1.setStudent_name(StudentName[k]);
                    ar1.setID(StudentID[k]);
                    results.add(ar1);

                }
                else
                {
                    StudentID[k] = "null";
                }
            }
            k++;
        }


        for(int j=k;j<hints.length;j++)
        {
            hints[j] = "";
        }
        return results;
    }
    private static ArrayList<Modelclass> GetPublisherResults()
    {
        ArrayList<Modelclass> results = new ArrayList<>();

        int k =0;
        if(StudentName[k] == null )
        {
            nodata.setVisibility(View.VISIBLE);
            list_students.setVisibility(View.GONE);
        }
        else {
            nodata.setVisibility(View.GONE);
            list_students.setVisibility(View.VISIBLE);
        }

        while (StudentName[k] != null)
        {
            Modelclass ar1 = new Modelclass();
            hints[k]=  StudentName[k];
            if (StudentisEnable[k].equals("1"))
            {

                ar1.setStudent_name(StudentName[k]);
                ar1.setID(StudentID[k]);
                AllData.add(StudentName[k]);
                SearchedStudentID.add(StudentID[k]);
                results.add(ar1);
            }

            k++;
        }


        for(int j=k;j<hints.length;j++)
        {
            hints[j] = "";
        }
        return results;
    }

    private void GetSearchedPublisher()
    {

        ArrayList<Modelclass> SearchedResults = new ArrayList<>();

        for (int k = 0; k < ListSize; k++)
        {


            Modelclass Searched = new Modelclass();
            String NameByName = AllData.get(k);
            //Log.e("Name", NameByName+"--------------------------------------------------------");
            //Log.e("Data", data+"--------------------------------------------------------");
            if(NameByName.startsWith(data.trim()))
            {
                Searched.setStudent_name(NameByName);
                Log.e("Searched ID", SearchedStudentID.get(k)+"--------------------------------------------------------");
                Searched.setID(SearchedStudentID.get(k));
                SearchedResults.add(Searched);
            }
        }

        if(SearchedResults.isEmpty()){

            AllData.clear();
            SearchedResults.clear();
            list = GetPublisherResults();
            //list_students.setAdapter(new ViewStudentShowAdaptor(getActivity(), list));
            adaptor = new ViewStudentShowAdaptor(getActivity(), list);
            list_students.setAdapter(adaptor);

        }

        else {

            //Log.e("Results", SearchedResults.size()+"--------------------------------------------------------");
            //list_students.setAdapter(new ViewStudentShowAdaptor(getActivity(), list));
            list = SearchedResults;
            adaptor = new ViewStudentShowAdaptor(getActivity(), list);
            list_students.setAdapter(adaptor);

        }

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

    public static void regenerate(String id)
    {
        adaptor = new ViewStudentShowAdaptor(c, GetPublisherResults(id));
        list_students.setAdapter(adaptor);
    }

    public static void refresh(String id)
    {
        list.clear();
        autoCompleteTextView.setText("");
        regenerate(id);
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
