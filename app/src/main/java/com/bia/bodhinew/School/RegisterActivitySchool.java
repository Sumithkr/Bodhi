package com.bia.bodhinew.School;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
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
import com.bia.bodhinew.Student.FetchFromDB;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivitySchool extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    EditText password, email, name, ConfirmPassword;
    Button register;
    SignInButton signInButton;
    String selectedState, selectedCity;
    Spinner SelectCity, SelectState;
    TextView OpenLogin;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 1;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    final ArrayList<String> StateList = new ArrayList<String>();
    final ArrayList<String> CityList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);

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

        SelectState = findViewById(R.id.SelectState);
        SelectCity = findViewById(R.id.SelectCity);

        AddUpAllStates();

        email= findViewById(R.id.SchoolEmail);
        password= findViewById(R.id.SchoolPassword);
        name= findViewById(R.id.SchoolName);
        ConfirmPassword= findViewById(R.id.SchoolConfirmPassword);
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

                startActivity(new Intent(getApplicationContext(), LoginActivitySchool.class));

            }
        });

        /*ArrayAdapter<String> StateAdapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_spinner,StateList){

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


        /*SelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedState = SelectState.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedState, Toast.LENGTH_LONG).show();
                AddUpAllCities();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SelectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //selectedCity= SelectCity.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedCity, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*SelectCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                return false;
            }
        });*/


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validation= email.getText().toString().trim();

                if(validation.equalsIgnoreCase("")){

                    email.setError("This field can not be empty");

                }

                else if(!validEmail(validation)){

                    email.setError("Enter a Valid Email ID");
                }

                else if(name.getText().toString().trim().equalsIgnoreCase("")){

                    name.setError("This field can not be empty");

                }

                else if(password.getText().toString().trim().equalsIgnoreCase("")){

                    password.setError("This field can not be empty");

                }

                else if(password.getText().toString().trim().length()!=7){

                    password.setError("Should be atleast 7 characters long");

                }

                else if(!ConfirmPassword.getText().toString().trim().equals(password.getText().toString().trim())){

                    password.setError("Password doesn't match");
                    ConfirmPassword.setError("Password doesn't match");

                }

                else if(selectedState.equals("") || selectedState.equals(null) || selectedState.equals("Select State")){

                    Toast.makeText(getApplicationContext(), "Select a state first", Toast.LENGTH_SHORT).show();

                }

                else if(selectedCity.equals("") || selectedCity.equals(null) || selectedCity.equals("Select City")){

                    Toast.makeText(getApplicationContext(), "Select a city first", Toast.LENGTH_SHORT).show();

                }

                else

                    StartProcessRegister();


            }
        });

    }

    public void StartProcessRegister(){

        String url = "https://bodhi.shwetaaromatics.co.in/School/Regsiter.php?Email="+email.getText().toString()+"&Password="+password.getText().toString()+
                "&Name="+name.getText().toString()+"&City="+selectedCity+"&State="+selectedState;

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
                    Intent main= new Intent(getApplicationContext(), LoginActivitySchool.class);
                    startActivity(main);
                    finish();
                }
                else if(obj.getString("result").equals("no"))
                {
                    name.setError("Try with some other username");
                }
                else if(obj.getString("result").equals("registered"))
                {
                    email.setError("Already Registered User !");
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


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

    public void AddUpAllStates(){

        StateList.clear();
        StateList.add("Select State");

        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("cities");

            for (int i = 0; i < m_jArry.length(); i++) {

                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String state = jo_inside.getString("state");

                if(StateList.contains(state)){}

                else {
                    StateList.add(state);
                }

            }

            ArrayAdapter<String> StateAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, StateList);
            StateAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
            SelectState.setAdapter(StateAdapter);
            SelectState.setOnItemSelectedListener(this);


        } catch (JSONException e) {

            e.printStackTrace();
        }

    }

    public void AddUpAllCities(){

        CityList.clear();
        CityList.add("Select City");

        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("cities");

            for (int i = 0; i < m_jArry.length() ; i++) {

                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String name = jo_inside.getString("name");
                String state = jo_inside.getString("state");

                if(selectedState.equals(state) && !selectedState.contains(name)) {

                    CityList.add(name);
                }

            }

            ArrayAdapter<String> CityAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_city, CityList);
            CityAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_city);
            SelectCity.setAdapter(CityAdapter);
            SelectCity.setOnItemSelectedListener(this);

        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResultBefore(result);
        }
    }

    private void handleSignInResultBefore(GoogleSignInResult result){
        if(result.isSuccess()){
            Toast.makeText(getApplicationContext(),"Successful Login",Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(RegisterActivitySchool.this, RegisterActivitySchool.class);
            //startActivity(intent);
            //gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
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

    private void gotoMainActivity(){
        Intent intent=new Intent(this, RegisterActivitySchool.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()  == R.id.SelectState){

            if(StateList.contains("Select State")) {

                StateList.remove(0);
                StateList.add(0, StateList.get(StateList.size() - 1));
                StateList.remove(StateList.size() - 1);
            }
            selectedState = SelectState.getItemAtPosition(position).toString();
            SelectState.setPrompt("Select State");
            //Toast.makeText(getApplicationContext(), selectedState, Toast.LENGTH_LONG).show();
            AddUpAllCities();

        }

        else if(parent.getId()== R.id.SelectCity){

            if(CityList.contains("Select City")) {

                CityList.remove(0);
                CityList.add(0, CityList.get(CityList.size() - 1));
                CityList.remove(CityList.size() - 1);
            }
            selectedCity= SelectCity.getItemAtPosition(position).toString();
            SelectCity.setPrompt("Select City");
            //Toast.makeText(getApplicationContext(), selectedCity, Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
