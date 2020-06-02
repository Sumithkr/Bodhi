package com.bia.bodhinew.School;

import android.os.Bundle;
import android.widget.EditText;

import com.bia.bodhinew.R;
import com.bia.bodhinew.Student.FetchFromDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateCredentialsSchool extends AppCompatActivity {

    EditText name, email, state, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_credentials_school);

        name= findViewById(R.id.name);
        email= findViewById(R.id.email);
        state= findViewById(R.id.state);
        city= findViewById(R.id.city);

        StartProcessUpdate();

    }

    public void StartProcessUpdate(){

        String url= "http://bodhi.shwetaaromatics.co.in/FetchProfileDetails.php?UserID="+file_retreive();

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
                email.setText("Email ID - "+ obj.getString("Email"));
                name.setText("Name - "+obj.getString("UserName"));
                city.setText("City - "+ obj.getString("SchoolCity"));
                state.setText("State - "+ obj.getString("SchoolState"));

            }

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
            inputStream = openFileInput("Bodhi_Login_School");
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
