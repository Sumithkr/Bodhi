package com.bia.bodhinew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OTP extends AppCompatActivity implements View.OnClickListener {

    Button verify,resend;
    EditText bt1,bt2,bt3,bt4;
    Intent i;
    String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        verify = findViewById(R.id.button28);
        resend = findViewById(R.id.Resend);
        verify.setOnClickListener(this);
        resend.setOnClickListener(this);
        bt1=(EditText)findViewById(R.id.editText3);
        bt2=(EditText)findViewById(R.id.editText34);
        bt3=(EditText)findViewById(R.id.editText35);
        bt4=(EditText)findViewById(R.id.editText36);
        i  = getIntent();
        OTP = i.getStringExtra("otp");

        bt1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String btnText = bt1.getText().toString();
                if(btnText == null || btnText.trim().equals(""))
                {

                }
                else
                {
                    Log.e("TAG",bt1.getText().toString()+" ss");
                    bt1.clearFocus();
                    bt2.requestFocus();
                }

            }
            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        bt2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String btnText = bt2.getText().toString();
                if(btnText == null || btnText.trim().equals(""))
                {

                }
                else
                {
                    Log.e("TAG",bt1.getText().toString()+" ss");
                    bt2.clearFocus();
                    bt3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        bt3.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String btnText = bt3.getText().toString();
                if(btnText == null || btnText.trim().equals(""))
                {

                }
                else
                {
                    Log.e("TAG",bt1.getText().toString()+" ss");
                    bt3.clearFocus();
                    bt4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

    }


    @Override
    public void onClick(View view) {

        if(view == verify){
            String number = bt1.getText().toString().trim() +bt2.getText().toString().trim() +bt3.getText().toString().trim() +bt4.getText().toString().trim() ;
            if(number.equals(OTP))
            {

            }
        }
    }
}
