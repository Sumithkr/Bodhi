package com.bia.bodhinew.Student;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bia.bodhinew.R;

import java.io.FileInputStream;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateCredentials extends AppCompatActivity {

    EditText Email, Name, State;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_credentials);

        Name= findViewById(R.id.name);
        Email= findViewById(R.id.email);
        State= findViewById(R.id.state);


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
