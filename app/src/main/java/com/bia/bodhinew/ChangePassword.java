package com.bia.bodhinew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bia.bodhinew.School.LoginActivitySchool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    EditText CurrentPassword,ChangePassword,ConfirmPassword;
    Button ChangePasswordButton,backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        CurrentPassword = (EditText)findViewById(R.id.CurrentPassword);
        ChangePassword = (EditText)findViewById(R.id.ChangePassword);
        ConfirmPassword = (EditText)findViewById(R.id.ConfirmPassword);
        ChangePasswordButton = (Button)findViewById(R.id.ChangePasswordButton);
        ChangePasswordButton.setOnClickListener(this);
        backbutton = findViewById(R.id.BackButton);
        backbutton.setOnClickListener(this);
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

    public void StartChangePasswordProcess(){

        String url = "https://bodhi.shwetaaromatics.co.in/ChangePassword.php?UserID="+file_retreive()+"&NewPassword="+ChangePassword.getText()+"&OldPassword="+CurrentPassword.getText();

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
                if(obj.getString("result").equals("yes"))
                {
                    String error = "error";
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = openFileOutput("Bodhi_Login_School", Context.MODE_PRIVATE);
                        outputStream.write(error.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent in = new Intent(getApplicationContext(), LoginActivitySchool.class);
                    startActivity(in);
                    finish();

                }

                else if (obj.getString("result").equals("PasswordNotMatched"))
                {
                    Toast.makeText(getApplicationContext(),"Please check your password",Toast.LENGTH_SHORT).show();
                    ChangePassword.setError("Something is wrong");
                    ConfirmPassword.setError("Something is wrong");

                }
                else if (obj.getString("result").equals("PasswordMatched"))
                {
                    Toast.makeText(getApplicationContext(),"Some error occured please try again",Toast.LENGTH_SHORT).show();
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view == ChangePasswordButton)
        {
            if(ChangePassword.getText().toString().trim().equalsIgnoreCase("")){

                ChangePassword.setError("This field can not be empty");
            }
            else if (CurrentPassword.getText().toString().trim().equalsIgnoreCase(""))
            {
                CurrentPassword.setError("This field can not be empty");
            }
            else if (ConfirmPassword.getText().toString().trim().equalsIgnoreCase(""))
            {
                ConfirmPassword.setError("This field can not be empty");
            }
            else {
                if (ChangePassword.getText().toString().trim().length()>6 && ConfirmPassword.getText().toString().trim().length()>6 ){
                    StartChangePasswordProcess();
                }
                  else {
                      ChangePassword.setError("Short Lenght");
                      ConfirmPassword.setError("Short Lenght");
                }
            }
        }

        if (view == backbutton)
        {
            onBackPressed();
        }
    }
}
