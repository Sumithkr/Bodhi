package com.bia.bodhinew.Student;

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
import com.bia.bodhinew.School.Modelclass;
import com.bia.bodhinew.School.StudentsFragment;
import com.bia.bodhinew.School.ViewStudentShowAdaptor;

import java.util.ArrayList;

public class Previously_watched_adaptor extends BaseAdapter {
    Context context;
    private static java.util.ArrayList<Modelclass> ArrayList;
    LayoutInflater inflater;


    public Previously_watched_adaptor(Context c, ArrayList<Modelclass> arrayList)
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
        return convertView;
    }

    static class ViewHolder {
        TextView student_name;
        ImageView student_icon;

    }


}


