package com.bia.bodhinew.School;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bia.bodhinew.R;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomePageRecyclerAdapterForVideosSchool extends RecyclerView.Adapter<HomePageRecyclerAdapterForVideosSchool.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static java.util.ArrayList<HomeDetailsGetandSetVideosSchool> ArrayList;
    private Context context;
    //vars
    /*private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;*/


    public HomePageRecyclerAdapterForVideosSchool(Context mContext, java.util.ArrayList<HomeDetailsGetandSetVideosSchool> homeClass) {

        ArrayList = homeClass;
        context = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_listview_videos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: called.");

        RequestOptions requestOptions= new RequestOptions();
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

        holder.EntityName.setText(ArrayList.get(position).getName());
        /*Bitmap VideoThumbnail = getBitmapFromURL(ArrayList.get(position).getThumbnailURL());
        Drawable VideoDrawable= new BitmapDrawable(VideoThumbnail);
        holder.EntityName.setBackgroundDrawable(VideoDrawable);*/
        holder.EntitySubjectName.setText(ArrayList.get(position).getSubjectName());
        holder.EntityDescription.setText(ArrayList.get(position).getDescription());

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

        TextView EntityName, EntitySubjectName, EntityDescription;

        public ViewHolder(View itemView) {

            super(itemView);

            EntityName = itemView.findViewById(R.id.EntityName);
            EntitySubjectName = itemView.findViewById(R.id.EntitySubjectName);
            EntityDescription = itemView.findViewById(R.id.EntityDescription);

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
