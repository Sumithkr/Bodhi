package com.bia.bodhinew.School;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;

import java.util.ArrayList;

public class ViewStudentShowAdaptor extends BaseAdapter {
    Context context;
    private static ArrayList<Modelclass> ArrayList;
    LayoutInflater inflater;


    public ViewStudentShowAdaptor(Context c, ArrayList<Modelclass> arrayList)
    {
        ArrayList = arrayList;
        inflater = LayoutInflater.from(c);
        context =c;
    }

    public int getCount() {
        return ArrayList.size();
    }

    public Object getItem(int position) {
        return ArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.school_view_students_list_design, null);
            holder = new ViewHolder();
            holder.student_name=(TextView)convertView.findViewById(R.id.student_name);
            //     ArrayList.get(position).setID();
            holder.student_icon= (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.student_name.setText(ArrayList.get(position).getStudent_name());
        Log.e("content",ArrayList.get(position).getStudent_name());
        holder.student_icon.setBackgroundResource(R.drawable.student);
        final ImageButton ok_delete = (ImageButton) convertView.findViewById(R.id.delete);
        ok_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String student_id = ArrayList.get(position).getID();
                Log.e("student_id",student_id);
                Delete_student(student_id);
            }
        });
        return convertView;
    }

    public void Delete_student(final String id)
    {

        String pack = "http://bodhi.shwetaaromatics.co.in/School/DisableStudent.php?UserID="+id;

        Log.e("pack",pack);
        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(pack,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {

                try
                {
                    Log.e("TAG","Selected Members Deleted.......");
                    StudentsFragment.refresh(id);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();

    }

    static class ViewHolder {
        TextView student_name;
        ImageView student_icon;

    }


}

