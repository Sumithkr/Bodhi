package com.bia.bodhinew.School;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bia.bodhinew.BuildConfig;
import com.bia.bodhinew.ChangePassword;
import com.bia.bodhinew.Feedback;
import com.bia.bodhinew.R;

import java.io.FileOutputStream;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class SettingsSchoolPage extends Fragment implements View.OnClickListener {

    CardView SchoolProfile,Card_ChangePassword,Card_Share,Card_Feedback,Card_ContactUs,Card_Logout,Card_UpgradePlan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView=  inflater.inflate(R.layout.school_settings_layout, container, false);

        SchoolProfile= RootView.findViewById(R.id.ProfileSchool);
        SchoolProfile.setOnClickListener(this);
        Card_ChangePassword= RootView.findViewById(R.id.Card_ChangePassword);
        Card_ChangePassword.setOnClickListener(this);
        Card_Share= RootView.findViewById(R.id.Card_Share);
        Card_Share.setOnClickListener(this);
        Card_Feedback= RootView.findViewById(R.id.Card_Feedback);
        Card_Feedback.setOnClickListener(this);
        Card_ContactUs= RootView.findViewById(R.id.Card_ContactUs);
        Card_ContactUs.setOnClickListener(this);
        Card_Logout= RootView.findViewById(R.id.Card_Logout);
        Card_Logout.setOnClickListener(this);
        Card_UpgradePlan= RootView.findViewById(R.id.Card_UpgradePlan);
        Card_UpgradePlan.setOnClickListener(this);


        return RootView;

    }

    @Override
    public void onClick(View view) {

        if (view == SchoolProfile)
        {
            startActivity(new Intent(getContext(), UpdateCredentialsSchool.class));
        }
        if (view == Card_ChangePassword)
        {
            startActivity(new Intent(getContext(), ChangePassword.class));
        }
        if (view == Card_Share)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "School Panel");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose One"));
        }
        if (view == Card_Feedback)
        {
            Intent i = new Intent(getActivity(), Feedback.class);
            startActivity(i);
            getActivity().finish();
        }
        if (view == Card_ContactUs)
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:7553883272"));
            startActivity(intent);
        }
        if (view == Card_Logout)
        {
            String error = "error";
            FileOutputStream outputStream = null;
            try {
                outputStream = getActivity().openFileOutput("Bodhi_Login_School", Context.MODE_PRIVATE);
                outputStream.write(error.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent in = new Intent(getActivity(),LoginActivitySchool.class);
            startActivity(in);
            getActivity().finish();
        }
        if (view == Card_UpgradePlan)
        {

        }

    }
}
