package com.bia.bodhinew.School;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

public class HomePageRecyclerAdapterForRevisionArticleSchool extends RecyclerView.Adapter<HomePageRecyclerAdapterForRevisionArticleSchool.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static java.util.ArrayList<HomeDetailsGetandSetRevisionArticleSchool> ArrayList;
    private Context context;
    ProgressDialog dialog;
    //vars
    /*private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;*/


    public HomePageRecyclerAdapterForRevisionArticleSchool(Context mContext, java.util.ArrayList<HomeDetailsGetandSetRevisionArticleSchool> homeClass) {

        ArrayList = homeClass;
        context = mContext;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_listview_revisionarticles_school, parent, false);
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

        if(ArrayList.get(position).getThumbnailURL().contains(".pdf")) {

            holder.ArticleIcon.setImageResource(R.drawable.pdf_files_icon);

        }

        holder.EntitySubjectName.setText(ArrayList.get(position).getSubjectName());
        holder.EntityDescription.setText(ArrayList.get(position).getDescription());

        holder.DeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteItem(position);

            }
        });

        holder.ArticleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filePath = ArrayList.get(position).getThumbnailURL() ;
                new DownloadFileFromURL().execute(filePath);

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
        ImageView ArticleIcon, DeleteIcon;

        public ViewHolder(View itemView) {

            super(itemView);

            EntityName = itemView.findViewById(R.id.EntityName);
            EntitySubjectName = itemView.findViewById(R.id.EntitySubjectName);
            EntityDescription = itemView.findViewById(R.id.EntityDescription);
            ArticleIcon= itemView.findViewById(R.id.ArticleIcon);
            DeleteIcon= itemView.findViewById(R.id.delete_icon);

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

        String url = "http://bodhi.shwetaaromatics.co.in/School/DisableMedia.php?UserID="+
                file_retreive()+"&MediaID="+ArrayList.get(position).getUploadID();

        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {

                try
                {
                    ConvertFromJSON(output);

                    HomePageSchool.initRecyclerViewRevisionArticle(ArrayList.get(position).getUploadID());

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
                Log.e("Result", obj.getString("result"));
                if(obj.getString("result").equals("yes"))
                {
                    Toast.makeText(context,"Item Deleted",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(context,"This item can not be deleted",Toast.LENGTH_SHORT).show();
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

    private void CheckFile( String filePath)
    {


        String url = filePath;

        Log.e("urlincheck",url);
        String dataType="";


        if (url.toString().contains(".doc") || url.toString().contains(".docx"))
        {
            // Word document
            dataType = "application/msword";
        }
        else if(url.toString().contains(".pdf"))
        {
            // PDF file
            dataType = "application/pdf";
        }
        else if(url.toString().contains(".ppt") || url.toString().contains(".pptx"))
        {
            // Powerpoint file
            dataType =  "application/vnd.ms-powerpoint";
        }
        else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            dataType = "application/vnd.ms-excel";
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar"))
        {
            // WAV audio file
            dataType = "application/x-wav";
        }
        else if(url.toString().contains(".jpg"))
        {
            // WAV audio file
            dataType = "image/jpg";
        }
        else if(url.toString().contains(".png"))
        {
            // WAV audio file
            dataType = "image/png";
        }
        openDocument(url, dataType);
    }

    private void openDocument(String path,String dataType)
    {
        File file = new File(path);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            uri = FileProvider.getUriForFile(context, "com.bia.bodhi.provider", file);
            Log.e("urihere", String.valueOf(uri));
        } else
        {
            uri = Uri.fromFile(file);
            Log.e("urihere", String.valueOf(uri));
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(uri, dataType);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Application not found", Toast.LENGTH_SHORT).show();
        }

    }


    class DownloadFileFromURL extends AsyncTask<String, String, String>
    {
        String extension = "";
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //  showDialog(progress_bar_type);
            //ShowDialog();
            dialog= new ProgressDialog(context);
            dialog.setMessage("Opening Article...");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();

        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try
            {


                if (f_url[0].contains("."))
                    extension = f_url[0].substring(f_url[0].lastIndexOf("."));

                URL url = new URL(f_url[0]);
                Log.e("LOG KA",f_url[0]);
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
                        .getExternalStorageDirectory().toString()
                        + "/file"+extension);


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //       publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage

            dialog.setMessage("Opening Article..." + progress[0]+ "%");

            Log.e("Progress - ", String.valueOf(Integer.parseInt(progress[0])));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url)
        {



            // dismiss the dialog after the file was downloaded
            //String x = Commons.getPath(Uri.parse(Environment.getExternalStorageDirectory().toString()+ "/doc.docx"), context);
            String x = Environment
                    .getExternalStorageDirectory().toString()
                    + "/file"+extension;
            CheckFile(x);
            dialog.dismiss();
            dialog.cancel();
        }

    }

}
