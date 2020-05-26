package com.bia.bodhinew;

import androidx.appcompat.app.AppCompatActivity;

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

public class Forgot_Password extends AppCompatActivity implements View.OnClickListener {
    EditText forgotPassword_NewPasswordConfirm,forgotPassword_NewPassword;
    Button forgotPassword_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        forgotPassword_NewPasswordConfirm = (EditText)findViewById(R.id.forgotPassword_NewPasswordConfirm);
        forgotPassword_NewPassword = (EditText)findViewById(R.id.forgotPassword_NewPassword);
        forgotPassword_button = (Button) findViewById(R.id.forgotPassword_button);
        forgotPassword_button.setOnClickListener(this);
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

        String url = "http://bodhi.shwetaaromatics.co.in/ForgetPassword.php?UserID="+file_retreive()+"&NewPassword="+forgotPassword_NewPassword.getText();

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
                    //Intent main= new Intent(getApplicationContext(), HomePage.class);
                    // startActivity(main);
                    // finish();

                     String error = "error";
                     FileOutputStream outputStream = null;
                     try {
                         outputStream = openFileOutput("Bodhi_Login_School", Context.MODE_PRIVATE);
                         outputStream.write(error.getBytes());
                         outputStream.close();
                         } catch (Exception e) {
                         e.printStackTrace();
                        }
                       Toast.makeText(getApplicationContext(),"Successfully changed",Toast.LENGTH_SHORT).show();
                       Intent in = new Intent(this, LoginActivitySchool.class);
                       startActivity(in);
                       finish();
                }

                else if (obj.getString("result").equals("no"))
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
    public void onClick(View view) {

        if (view == forgotPassword_button)
        {
            if(forgotPassword_NewPassword.getText().toString().trim().equalsIgnoreCase("")){

                forgotPassword_NewPassword.setError("This field can not be empty");
            }
            else if (forgotPassword_NewPasswordConfirm.getText().toString().trim().equalsIgnoreCase(""))
            {
                forgotPassword_NewPasswordConfirm.setError("This field can not be empty");
            }
            else {
                if (forgotPassword_NewPassword.getText().toString().trim().length()<7
                   && forgotPassword_NewPasswordConfirm.getText().toString().trim().length()<7 ){
                    StartChangePasswordProcess();
                }
                else {
                    forgotPassword_NewPassword.setError("Short Lenght");
                    forgotPassword_NewPasswordConfirm.setError("Short Lenght");
                }
            }
        }

    }
}
