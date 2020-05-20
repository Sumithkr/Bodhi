package com.bia.bodhi.Student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bia.bodhi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(file_retreive().equals("error") == true ||file_retreive().equals("") == true || file_retreive() == null){

        }
        else{

            Intent success = new Intent(LoginActivity.this, HomePage.class);
            success.putExtra("Login", file_retreive());
            startActivity(success);
            finish();

        }

        password = findViewById(R.id.password);
        email = findViewById(R.id.username);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().trim().equalsIgnoreCase("")){

                    email.setError("This field can not be empty");

                }

                else if(password.getText().toString().trim().equalsIgnoreCase("")){

                    password.setError("This field can not be empty");

                }

                else

                    StartProcessLogin();


            }
        });

    }

    public void StartProcessLogin(){

        String url = "https://shwetaaromatics.co.in/ca/student/login_check.php?email="+email.getText()+"&password="+password.getText();

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
                    file_write_url(email.getText().toString().trim());

                    Intent main= new Intent(getApplicationContext(), HomePage.class);
                    main.putExtra("Login", email.getText().toString());
                    startActivity(main);
                    finish();

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void file_write_url(String username)
    {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("Bodhi_Login", Context.MODE_PRIVATE);
            outputStream.write(username.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
