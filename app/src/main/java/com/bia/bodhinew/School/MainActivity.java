package com.bia.bodhinew.School;

import android.os.Bundle;
import android.widget.ListView;

import com.bia.bodhinew.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
 ListView listView;
 String name[] = {"rohan","mohan","sohan","ram","shyam","dholakram"};
 String Class[]={"1st","2nd","3rd","4th","5th","6th"};
    ArrayList<model> Open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.list);
        Open = GetPublisherResults();
        listView.setAdapter(new myadaptor(getApplicationContext(), Open));
    }

    private ArrayList<model> GetPublisherResults()
    {
        ArrayList<model> results = new ArrayList<>();
        int k =0;
        while (k<name.length)
        {
            model ar1 = new model();
            ar1.setItem_name(name[k]);
            ar1.setItem_class(Class[k]);
            results.add(ar1);
            k++;
        }

        return results;
    }
}
