package com.bia.bodhinew.School;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SchoolNoticeShowAdaptor extends BaseAdapter {
      Context context;
      private static ArrayList<Modelclass> ArrayList;
      LayoutInflater inflater;


    public SchoolNoticeShowAdaptor(Context c, ArrayList<Modelclass> arrayList)
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
            convertView = inflater.inflate(R.layout.noticefragment_school_listview_design, null);
            holder = new ViewHolder();
            holder.content_of_notice=(TextView)convertView.findViewById(R.id.content_of_notice);
            //     ArrayList.get(position).setID();
           holder.datetime_of_notice =(TextView)convertView.findViewById(R.id.datetime_of_notice);
            holder.msg_notice_image= (ImageView) convertView.findViewById(R.id.notice_img);

            //holder.msg_notice_image.setImageBitmap(notice_image_main[poistion]);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content_of_notice.setText(ArrayList.get(position).getContent_of_notice());
        Log.e("content",ArrayList.get(position).getContent_of_notice());
        holder.datetime_of_notice.setText(ArrayList.get(position).getDatetime_of_notice());

        if (ArrayList.get(position).getBoolImage() == true)
        {

            if (ArrayList.get(position).getImg_of_notice().contains(".docx") || ArrayList.get(position).getImg_of_notice().contains(".pdf"))
            {
                final ImageView icon_for_attachment = (ImageView) convertView.findViewById(R.id.icon_for_attachment);
                icon_for_attachment.setVisibility(View.VISIBLE);
                icon_for_attachment.setBackgroundResource(R.drawable.attachment);
                icon_for_attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            else{
                holder.msg_notice_image.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(ArrayList.get(position).getImg_of_notice())
                        .resize(300, 300)
                        .centerCrop()
                        .into(holder.msg_notice_image);
                Picasso.with(context).setLoggingEnabled(true);
                Log.e("img", String.valueOf(ArrayList.get(position).getImg_of_notice().contains(".jpg")));
            }

        }
        else
        {
            //holder.msg_notice_image.setImageDrawable(context.getResources().getDrawable(R.drawable.last));
             holder.msg_notice_image.setVisibility(View.GONE);
        }

        //holder.msg_notice_image.setImageBitmap(ArrayList.get(position).getNotice_image());
        return convertView;
    }

    static class ViewHolder {
        TextView content_of_notice,datetime_of_notice;
        ImageView msg_notice_image;

    }
}
