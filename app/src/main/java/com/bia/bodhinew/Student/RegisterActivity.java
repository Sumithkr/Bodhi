package com.bia.bodhinew.Student;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    EditText password, email, name;
    Button register;
    SignInButton signInButton;
    String selectedSchool, selectedClass;
    Spinner SelectClass, SelectSchool;
    TextView OpenLogin;
    int allotedState= 0, allotedCity= 0;
     ACProgressPie dialog;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 1;

    final List<String> SchoolList = new ArrayList<String>();
    final List<String> ClassList = new ArrayList<String>();

    String[] SchoolID= new String[1000];
    String MainSchoolID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.parseColor("#fa3a0f"));

        OpenLogin= findViewById(R.id.OpenLogin);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        SelectSchool = findViewById(R.id.SelectSchool);
        SelectClass = findViewById(R.id.SelectClass);

        AddUpAllSchool();

        email= findViewById(R.id.StudentEmail);
        password= findViewById(R.id.StudentPassword);
        name= findViewById(R.id.StudentName);
        register= findViewById(R.id.register);

        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        TextView GoogleButtonText = (TextView) signInButton.getChildAt(0);
        GoogleButtonText.setText("Sign up with Google");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);

            }
        });

        OpenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

        /*ArrayAdapter<String> StateAdapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_spinner,SchoolList){

            @Override
            public boolean isEnabled(int position) {
                return super.isEnabled(position);
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View RootView=  super.getDropDownView(position, convertView, parent);

                TextView tv= (TextView) RootView;

                if(position==0){

                    tv.setTextColor(Color.GRAY);
                    tv.setEnabled(false);

                }

                return  RootView;

            }
        };*/


        /*SelectSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedSchool = SelectSchool.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedSchool, Toast.LENGTH_LONG).show();
                AddpUpAllClass();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SelectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //selectedClass= SelectClass.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedClass, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*SelectClass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                return false;
            }
        });*/


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().trim().equalsIgnoreCase("")){

                    email.setError("This field can not be empty");

                }

                else if(name.getText().toString().trim().equalsIgnoreCase("")){

                    password.setError("This field can not be empty");

                }

                else if(password.getText().toString().trim().equalsIgnoreCase("")){

                    password.setError("This field can not be empty");

                }

                else

                    StartProcessRegister();

            }
        });

    }

    public void progdialog()
    {
        dialog = new ACProgressPie.Builder(this)
                .ringColor(Color.parseColor("#fa3a0f"))
                .pieColor(Color.parseColor("#fa3a0f"))
                .bgAlpha(1)
                .bgColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.show();
    }

    public void StartProcessRegister(){
            progdialog();
        String url = "https://bodhi.shwetaaromatics.co.in/Student/Register.php?SchoolID="+MainSchoolID+"&UserName="+name.getText().toString()+
                "&Class="+selectedClass+"&Email="+email.getText().toString()+"&Password="+password.getText().toString();

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
       dialog.dismiss();
       dialog.cancel();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void AddpUpAllClass(){

        ClassList.clear();
        ClassList.add(0, "Select Class");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            int statusCode = result.getStatus().getStatusCode();
            Log.e("Guide", String.valueOf(statusCode));
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            finish();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            //gotoProfile();
        }else{

            Toast.makeText(getApplicationContext(), "Sign In Cancel", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResultAfter(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResultAfter(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResultAfter(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            name.setText(account.getDisplayName());
            email.setText(account.getEmail());


        }else{

            //gotoMainActivity();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()  == R.id.SelectSchool){

            if(SchoolList.contains("Select School")) {

                SchoolList.remove(0);
                SchoolList.add(0, SchoolList.get(SchoolList.size() - 1));
                SchoolList.remove(SchoolList.size() - 1);

            }
            selectedSchool = SelectSchool.getItemAtPosition(position).toString();
            SelectSchool.setPrompt("Select School");
            MainSchoolID= SchoolID[position];
            AddpUpAllClass();
            Toast.makeText(getApplicationContext(), selectedSchool, Toast.LENGTH_LONG).show();

        }

        else if(parent.getId()== R.id.SelectClass){

            if(ClassList.contains("Select Class")) {

                ClassList.remove(0);
                ClassList.add(0, ClassList.get(ClassList.size() - 1));
                ClassList.remove(ClassList.size() - 1);

            }

            selectedClass= SelectClass.getItemAtPosition(position).toString();
            SelectClass.setPrompt("Select Class");
            SelectClass.setSelection(position);
            //Toast.makeText(getApplicationContext(), selectedClass, Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void AddUpAllSchool()
    {
        String url = "https://bodhi.shwetaaromatics.co.in/Student/SchoolFetch.php";

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                try
                {
                    ConvertFromJSONList(output);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    private void ConvertFromJSONList(String json)
    {
        try
        {

            SchoolList.add(0, "Select School");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                SchoolList.add(obj.getString("UserName"));
                SchoolID[i] = obj.getString("SchoolID");

            }

            ArrayAdapter<String> SchoolAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, SchoolList);
            SchoolAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
            SelectSchool.setAdapter(SchoolAdapter);
            SelectSchool.setOnItemSelectedListener(this);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
