package com.bia.bodhinew.School;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.bia.bodhinew.Student.FetchFromDB;
import com.bumptech.glide.request.RequestOptions;
import com.folioreader.FolioReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomePageRecyclerAdapterForBooksSchool extends RecyclerView.Adapter<HomePageRecyclerAdapterForBooksSchool.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static java.util.ArrayList<HomeDetailsGetandSetBooksSchool> ArrayList;
    private Context context;
    ProgressDialog download_dialog;
    String file_substring;
    FolioReader folioReader = FolioReader.get();
    //vars
    /*private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;*/


    public HomePageRecyclerAdapterForBooksSchool(Context mContext, java.util.ArrayList<HomeDetailsGetandSetBooksSchool> homeClass) {

        ArrayList = homeClass;
        context = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_listview_books_school, parent, false);
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

        holder.DeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteItem(position);

            }
        });

        holder.OpenBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                file_substring = ArrayList.get(position).getURL().substring(ArrayList.get(position).getURL().indexOf("/Media/")+7);
                File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + file_substring);

                if(!file.exists()){

                    Log.e("File not exist", "yes");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        new DownloadFileFromURL().execute(ArrayList.get(position).getURL());
                    }

                }

                else {

                    Log.e("File not exist", "NO");
                    folioReader.openBook(Environment.getExternalStorageDirectory().toString() + "/" + file_substring);

                }

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

        TextView EntityName, EntitySubjectName, EntityDescription;
        ImageView DeleteIcon, OpenBook;

        public ViewHolder(View itemView) {

            super(itemView);

            EntityName = itemView.findViewById(R.id.EntityName);
            EntitySubjectName = itemView.findViewById(R.id.EntitySubjectName);
            EntityDescription = itemView.findViewById(R.id.EntityDescription);
            DeleteIcon= itemView.findViewById(R.id.delete_icon);
            OpenBook= itemView.findViewById(R.id.open_book);

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


    public void DeleteItem(final int position){

        Log.e("Upload ID", ArrayList.get(position).getUploadID());

        String url = "https://bodhi.shwetaaromatics.co.in/School/DisableMedia.php?UserID="+
                file_retreive()+"&MediaID="+ArrayList.get(position).getUploadID();

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {

                try
                {
                    ConvertFromJSON(output);

                    HomePageSchool.initRecyclerViewBooks(ArrayList.get(position).getUploadID());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();

    }

    public void ConvertFromJSON(String json){

        try
        {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                if(obj.getString("result").equals("yes"))
                {
                    Toast.makeText(context,"Item Deleted",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(context,"Item Not Deleted",Toast.LENGTH_SHORT).show();
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private String file_retreive()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = context.openFileInput("Bodhi_Login_School");
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

    class DownloadFileFromURL extends AsyncTask<String, String, String>
    {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showDialog(progress_bar_type);
            Log.e("Start","Started");
            //ShowDialog();

            download_dialog= new ProgressDialog(context);
            download_dialog.setMessage("Fetching Book...");
            download_dialog.setCancelable(false);
            download_dialog.setInverseBackgroundForced(false);
            download_dialog.show();

        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()+ "/" + file_substring);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1)
                {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    }

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress)
        {
            // setting progress percentage

            download_dialog.setMessage("Fetching Book....");

            Log.e("Percent - "," - "+ progress[0]);
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {

            DismissDialog();

            folioReader.openBook(Environment.getExternalStorageDirectory().toString()+"/"+file_substring);

        }

    }

    public void DismissDialog(){

        download_dialog.dismiss();
        download_dialog.cancel();

    }

}
