package com.bia.bodhinew.Student;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import androidx.cardview.widget.CardView;

public class Previously_watched_adaptor extends BaseAdapter {
    Context context;
    private static java.util.ArrayList<Modelclass> ArrayList;
    LayoutInflater inflater;
    ImageView imageView;


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
            convertView = inflater.inflate(R.layout.priviously_watched_list_design, null);
            holder = new ViewHolder();
            holder.file_name=(TextView)convertView.findViewById(R.id.FileName);
            holder.file_description=(TextView)convertView.findViewById(R.id.FileDescription);
            holder.file_datetime=(TextView)convertView.findViewById(R.id.FileDatetime);
            holder.subjectname=(TextView)convertView.findViewById(R.id.SubjectName);
            //     ArrayList.get(position).setID();
            imageView = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.file_name.setText(ArrayList.get(position).getFile_name());
        holder.file_description.setText(ArrayList.get(position).getFile_description());
        holder.file_datetime.setText(ArrayList.get(position).getDatetime_of_notice());
        holder.subjectname.setText(ArrayList.get(position).getSubject_name());
        if (ArrayList.get(position).getBoolImage() == true){
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(ArrayList.get(position).getImg_of_notice()), "image/*");
                    context.startActivity(intent);
                }
            });
        }
        else {
            imageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView file_name,file_description,file_datetime,subjectname;

    }


}


