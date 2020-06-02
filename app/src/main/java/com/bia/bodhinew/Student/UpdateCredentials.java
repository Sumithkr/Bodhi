package com.bia.bodhinew.Student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bia.bodhinew.R;

import java.io.FileInputStream;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateCredentials extends AppCompatActivity {

    EditText email, password;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_credentials);

        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        update= findViewById(R.id.update);



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(update.getText().toString().equals("Update")){

                    update.setText("Save");
                    email.setEnabled(true);
                    password.setEnabled(true);

                }

                else {

                    update.setText("Update");
                    email.setEnabled(false);
                    password.setEnabled(false);

                }

            }
        });

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
