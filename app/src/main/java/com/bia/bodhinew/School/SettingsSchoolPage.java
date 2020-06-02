package com.bia.bodhinew.School;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bia.bodhinew.R;
import com.bia.bodhinew.Student.UpdateCredentials;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class SettingsSchoolPage extends Fragment {

    CardView SchoolProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView=  inflater.inflate(R.layout.school_settings_layout, container, false);

        SchoolProfile= RootView.findViewById(R.id.ProfileSchool);

        SchoolProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), UpdateCredentialsSchool.class));

            }
        });

        return RootView;

    }

}
