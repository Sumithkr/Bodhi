package com.bia.bodhi.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bia.bodhi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText password, email;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        register= findViewById(R.id.register);

        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.list)
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String selectedItem = spinner.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().trim().equalsIgnoreCase("")){

                    email.setError("This field can not be empty");

                }

                else if(password.getText().toString().trim().equalsIgnoreCase("")){

                    password.setError("This field can not be empty");

                }

                else

                    StartProcessRegister();

            }
        });

    }

    public void StartProcessRegister(){

        String school_id = "KCM";
        String name = "Stuart";
        String url = "https://shwetaaromatics.co.in/ca/student/register.php?email="+email.getText()+"&school_id="+school_id+"&password="+password.getText()+"&name="+name;

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
                    Intent main= new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(main);
                    finish();
                }
                else if(obj.getString("result").equals("no"))
                {
                    Toast.makeText(getApplicationContext(),"Some error occured",Toast.LENGTH_SHORT).show();
                }
                else if(obj.getString("result").equals("registered"))
                {
                    Toast.makeText(getApplicationContext(),"Already Registered User !" +
                            "",Toast.LENGTH_SHORT).show();
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


}
