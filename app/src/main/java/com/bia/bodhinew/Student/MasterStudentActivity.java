package com.bia.bodhinew.Student;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MasterStudentActivity extends AppCompatActivity {

    HomePage home = new HomePage();
    public static String currentFragment = null;

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.Home:
                    fragment = new HomePage();
                    break;

                case R.id.Notice:
                    fragment = new StudentNoticePage();
                    break;

                case R.id.Setting:
                    fragment = new SettingsStudentPage();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_master_layout);
        loadFragment(home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);*/

        displayPreviousFragment(currentFragment);

    }

    public void displayPreviousFragment(String currentFragment)
    {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (currentFragment)
        {
            case "SubjectPage" :

                fragment = new HomePage();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();
                break;

            case "Master_Activity" :

                super.onBackPressed();
                Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
                Log.e("Back Pressed", "Yes");
                break;

        }

        if (fragment != null) {

        }
    }
}
