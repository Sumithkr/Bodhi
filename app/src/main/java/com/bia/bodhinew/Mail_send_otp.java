package com.bia.bodhinew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class Mail_send_otp extends AppCompatActivity {

    Button submit;
    EditText forgotmail;
    String usermail;
    final int min = 1000;
    final int max = 10000;
    final int random = new Random().nextInt((max - min) + 1) + min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_send_otp);forgotmail = findViewById(R.id.forgotmail);
        usermail = forgotmail.getText().toString();
        submit = (Button)this.findViewById(R.id.submit);
        Log.e("hdvj", String.valueOf(random));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Forgotpassword.this, otp.class));
                Intent i  = new Intent(Mail_send_otp.this,OTP.class);
                i.putExtra("otp",random);
                startActivity(i);
            }
        });
    }
}
