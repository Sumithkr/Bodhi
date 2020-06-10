package com.bia.bodhinew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Feedback extends AppCompatActivity {
    Button send;
    EditText what,contact;
    TextView success;
    ProgressDialog pd;
    String str_Check=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }
    public void send_msg(View view){

        send = (Button)findViewById(R.id.button15);
        what = (EditText)findViewById(R.id.editText6);
        contact = (EditText)findViewById(R.id.editText22);
        //  success = (TextView)findViewById(R.id.textView32);

        String mat = what.getText().toString();
        String con = contact.getText().toString();

        if(!mat.equals("") && !con.equals("")){

            this.pd = ProgressDialog.show(this, "Loading...", "Please wait..", true, false);
            AsyncTask<InputStream, String, String> msg = new AsyncTask<InputStream, String, String>() {
                @Override
                protected String doInBackground(InputStream... inputStreams) {
                    feedback_entry();
                    return null;
                }

                @Override
                protected void onPostExecute(String s)
                {
                    if(str_Check.equals("Message sent!"))
                    {
                        Toast.makeText(getApplicationContext(), "Thankyou for your feedback", Toast.LENGTH_SHORT).show();
                        what.setText("");
                        contact.setText("");
                        pd.dismiss();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Network error. Retry again", Toast.LENGTH_SHORT).show();

                    }
                    super.onPostExecute(s);
                }
            };
            msg.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
    }

    public  void feedback_entry()
    {
        String save_url = "https://boxinall.in/additionalphp/phpmailer-master/examples/bl_feedback.php";
        try

        {
            URL url = new URL(save_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(what.getText().toString().trim()+" , "+contact.getText().toString().trim(), "UTF-8");

            ;

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String res;
            while ((res=bufferedReader.readLine()) != null)
            {
                str_Check = res;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        }
        catch(Exception e)

        {
            e.printStackTrace();
        }
    }
}

