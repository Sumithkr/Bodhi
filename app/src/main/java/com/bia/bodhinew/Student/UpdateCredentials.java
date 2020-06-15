package com.bia.bodhinew.Student;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bia.bodhinew.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateCredentials extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText Email, Name, School, Class;
    Button Update;
    ImageButton BackButton;
    Spinner SelectClass;
    String selectedClass;
    final List<String> ClassList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_credentials);

        Name= findViewById(R.id.name);
        Email= findViewById(R.id.email);
        School= findViewById(R.id.School);
        BackButton= findViewById(R.id.BackButton);
        SelectClass= findViewById(R.id.SelectClass);
        Update= findViewById(R.id.UpdateCredentials);
        SelectClass.setEnabled(false);
        SelectClass.setClickable(false);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Update.getText().toString().equals("Save")) {

                    Log.e("Adding..","0");

                    UpdateClass();
                    SelectClass.setEnabled(false);
                    SelectClass.setClickable(false);
                    Update.setText("Update");

                }
                else {

                    SelectClass.setEnabled(true);
                    SelectClass.setClickable(true);
                    AddpUpAllClass();
                    Update.setText("Save");

                }

            }
        });

        StartProcessUpdate();

    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }

    public void StartProcessUpdate(){

        String url= "https://bodhi.shwetaaromatics.co.in/FetchProfileDetails.php?UserID="+file_retreive();

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

    public void ConvertFromJSON(String json){

        try
        {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                Email.setText("Email ID - "+ obj.getString("Email"));
                Name.setText("Name - "+obj.getString("UserName"));
                School.setText("School - "+ obj.getString("SchoolName"));
                selectedClass= obj.getString("Class");
                Toast.makeText(getApplicationContext(), selectedClass, Toast.LENGTH_LONG).show();

            }

            Log.e("Data", "Updated");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void UpdateClass(){

        String url= "https://bodhi.shwetaaromatics.co.in/Student/UpdateSchoolClass.php?Class="+selectedClass+"&UserID="+file_retreive();

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {

                try
                {
                    Log.e("Adding..","2");
                    Log.e("Adding..",selectedClass);
                    Toast.makeText(UpdateCredentials.this, "Your class is updated", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.SelectCity ){


                selectedClass= SelectClass.getItemAtPosition(position).toString();
                SelectClass.setPrompt("Select Class");
                SelectClass.setSelection(position);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*public void UpdateSpinner(){

        ClassList.clear();
        ClassList.add(selectedClass);
        ArrayAdapter<String> ClassAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_city, ClassList);
        ClassAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_city);
        SelectClass.setAdapter(ClassAdapter);

    }*/


    public void AddpUpAllClass(){

        ClassList.clear();

        Log.e("Adding..","1");

        try {

            for (int i = 1; i < 13 ; i++) {

                ClassList.add(String.valueOf(i));

            }

            ArrayAdapter<String> ClassAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_city, ClassList);
            ClassAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_city);
            SelectClass.setAdapter(ClassAdapter);
            SelectClass.setOnItemSelectedListener(this);


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

}
