package com.bia.bodhinew.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bia.bodhinew.R;

import androidx.fragment.app.Fragment;


public class StudentNoticePage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.student_notice_layout, container, false);
    }

}
