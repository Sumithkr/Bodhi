package com.bia.bodhinew.Student;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoInfo;

public class HomePageRecyclerAdapterForVideos extends RecyclerView.Adapter<HomePageRecyclerAdapterForVideos.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static java.util.ArrayList<HomeDetailsGetandSetVideos> ArrayList;
    private Context context;
    ArrayList<String> FileID= new ArrayList<>();
    //vars
    /*private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;*/


    public HomePageRecyclerAdapterForVideos(Context mContext, java.util.ArrayList<HomeDetailsGetandSetVideos> homeClass) {

        ArrayList = homeClass;
        context = mContext;
        setHasStableIds(true);
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

        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner(ArrayList.get(position).getThumbnailURL(),holder);
        asyncTaskRunner.execute();

        holder.EntityName.setText(ArrayList.get(position).getName());
        holder.EntitySubjectName.setText(ArrayList.get(position).getSubjectName());
        holder.EntityDescription.setText(ArrayList.get(position).getDescription());

        holder.EntityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StartServerFile(ArrayList.get(position).getUploadID());
                GiraffePlayer.play(context, new VideoInfo(String.valueOf(ArrayList.get(position).getURL())));

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

    @Override
    public long getItemId(int position)
    {
        //file_size.setText( position+"");
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        String Imageurl;
        Bitmap bitmap;
        ViewHolder holder;

        public AsyncTaskRunner(String Imageurl, ViewHolder holder) {
            this.Imageurl = Imageurl;
            this.holder = holder;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL(Imageurl);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());


            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            Drawable VideoDrawable= new BitmapDrawable(bitmap);
            holder.EntityName.setBackgroundDrawable(VideoDrawable);


        }
    }

    public void StartServerFile(String MediaID)
    {
        String url = "https://bodhi.shwetaaromatics.co.in/Student/UpdatePreviouslyWatched.php?UserID="+file_retreive()+"&MediaID="+MediaID;
        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {

                try
                {

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    private String file_retreive()
    {
        FileInputStream inputStream = null;
        try {

            inputStream = context.openFileInput("Bodhi_Login");
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int n;
            while (( n = inputStream.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }

            inputStream.close();
            return fileContent.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "error";
        }
    }

}
