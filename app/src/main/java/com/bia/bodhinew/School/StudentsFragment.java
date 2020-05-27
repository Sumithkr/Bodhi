package com.bia.bodhinew.School;

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
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;

import androidx.fragment.app.Fragment;


public class StudentsFragment extends Fragment {
    String[] Class_list = { "Select Class","1", "2", "3", "4", "5","6","7","8","9","10","11","12" };
    String Cls;
    ImageView nodata;
    ListView list_students;
    String[] StudentName = new String[1000];
    String[] StudentID = new String[1000];
    String[] StudentEmail = new String[1000];
    String[] StudentDateTime = new String[1000];
    String[] StudentisEnable = new String[1000];
    String data;
    private ViewStudentShowAdaptor adaptor;
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<Modelclass> list;
    String[] hints= new String[100];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_students, container, false);
        //StartServerFile();
        nodata = (ImageView)v.findViewById(R.id.nodata);
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
        //
    }

    public void StartServerFile()
    {

        String url = "http://bodhi.shwetaaromatics.co.in/School/FetchStudents.php?Class="+Cls+"&UserID="+file_retreive();
        Log.e("url",url);
        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                //this function executes after
                Toast.makeText(getActivity(),"END",Toast.LENGTH_SHORT).show();
                try
                {
                    ConvertFromJSON(output);
                    list = GetPublisherResults();
                    //list_students.setAdapter(new ViewStudentShowAdaptor(getActivity(), list));
                    adaptor = new ViewStudentShowAdaptor(getActivity(), list);
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
                Log.e("name",StudentName[i]);
                StudentID[i]=obj.getString("UserID");
                Log.e("id",StudentID[i]);
                StudentEmail[i] = obj.getString("Email");
                Log.e("email",StudentEmail[i]);
                StudentDateTime[i] = obj.getString("DateTime");
                Log.e("datetime",StudentDateTime[i]);
                StudentisEnable[i] = obj.getString("isEnable");
                Log.e("active",StudentisEnable[i]);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList<Modelclass> GetPublisherResults()
    {
        ArrayList<Modelclass> results = new ArrayList<>();
        int k =0;
        if(StudentName[k] == null)
        {
            nodata.setVisibility(View.VISIBLE);
            list_students.setVisibility(View.GONE);
        }

        while (StudentName[k] != null)
        {
            nodata.setVisibility(View.GONE);
            list_students.setVisibility(View.VISIBLE);
            Modelclass ar1 = new Modelclass();
            hints[k]=  StudentName[k];
            if (StudentisEnable[k].equals("1"))
            {
                ar1.setStudent_name(StudentName[k]);
                ar1.setID(StudentID[k]);
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
        int k =0;

        for ( k = 0; k < list.size(); k++)
        {

            String x = list.get(k).getStudent_name();
            if(x.startsWith(data.trim()) == true)
            {
                list_students.setSelection(k);
            }
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
}
