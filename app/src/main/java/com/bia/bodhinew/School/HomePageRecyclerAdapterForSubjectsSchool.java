package com.bia.bodhinew.School;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class HomePageRecyclerAdapterForSubjectsSchool extends RecyclerView.Adapter<HomePageRecyclerAdapterForSubjectsSchool.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static java.util.ArrayList<HomeDetailsGetandSetSubjectsSchool> ArrayList;
    private Context context;
    SubjectPage NextSubject= new SubjectPage();


    public HomePageRecyclerAdapterForSubjectsSchool(Context mContext, java.util.ArrayList<HomeDetailsGetandSetSubjectsSchool> homeClass) {

        ArrayList = homeClass;
        context = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_listview_subjects, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: called.");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.ic_launcher_background);

        /*Glide.with(mContext)
                .load(mImageUrls.get(position))
                .apply(requestOptions)
                .thumbnail(Glide.with(mContext).load(mImageUrls.get(position)))
                .into(holder.image);*/

        /*Glide.with(mContext)
                .load(mImageUrls.get(position))
                .asBitmap()
                .into(holder.image);*/

        /*Bitmap VideoThumbnail = getBitmapFromURL(ArrayList.get(position).getThumbnailURL());
        Drawable VideoDrawable= new BitmapDrawable(VideoThumbnail);
        holder.EntityName.setBackgroundDrawable(VideoDrawable);*/
        holder.EntitySubjectName.setText(ArrayList.get(position).getSubjectName());

        if (ArrayList.get(position).getSubjectName().contains("ENGLISH")) {

            holder.SubjectIcon.setImageResource(R.drawable.english_icon);

        } else if (ArrayList.get(position).getSubjectName().contains("CHEMISTRY")) {

            holder.SubjectIcon.setImageResource(R.drawable.chemistry_icon);

        } else if (ArrayList.get(position).getSubjectName().contains("MATHS")) {

            holder.SubjectIcon.setImageResource(R.drawable.mathmatics_icon);

        }

        else {

            holder.SubjectIcon.setImageResource(R.drawable.othersubjects_icon);

        }

        holder.SubjectClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle get_Credentials = new Bundle();
                get_Credentials.putString("ForSubject", ArrayList.get(position).getSubjectName());
                NextSubject.setArguments(get_Credentials);

                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, NextSubject);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        /*holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + ArrayList.get(position));
                Toast.makeText(mContext, ArrayList.get(position), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        try {
            return ArrayList.size();

        }catch(Exception e){

            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView EntitySubjectName;
        ImageView SubjectIcon;
        LinearLayout SubjectClick;

        public ViewHolder(View itemView) {

            super(itemView);

            EntitySubjectName = itemView.findViewById(R.id.EntitySubjectName);
            SubjectIcon = itemView.findViewById(R.id.SubjectIcon);
            SubjectClick= itemView.findViewById(R.id.SubjectClick);

        }
    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
