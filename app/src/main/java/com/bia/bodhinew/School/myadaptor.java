package com.bia.bodhinew.School;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;

import java.util.ArrayList;

public class myadaptor extends BaseAdapter {
      Context context;
      private static ArrayList<model> ArrayList;
      LayoutInflater inflater;

    public myadaptor(Context c, ArrayList<model> arrayList)
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
        final myadaptor.ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.view, null);
            holder = new ViewHolder();
            holder.item_name=(TextView)convertView.findViewById(R.id.item_name);
            //     ArrayList.get(position).setID();

            final Button ok_open = (Button)convertView.findViewById(R.id.button);
            ok_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String book_url = ArrayList.get(position).getItem_class();
                    Log.e("URL___",book_url+"---");
                    Toast.makeText(context, "class"+book_url, Toast.LENGTH_SHORT).show();

                }
            });


            convertView.setTag(holder);
        } else {
            holder = (myadaptor.ViewHolder) convertView.getTag();
        }

        holder.item_name.setText(ArrayList.get(position).getItem_name());
        return convertView;
    }

    static class ViewHolder {
        TextView item_name;
        TextView item_class;


    }
}
