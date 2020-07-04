package com.bia.bodhinew.Student;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.FetchFromDB;
import com.bia.bodhinew.R;
import com.bia.bodhinew.School.Modelclass;
import com.bia.bodhinew.School.StudentsFragment;
import com.bia.bodhinew.School.ViewStudentShowAdaptor;
import com.folioreader.FolioReader;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

public class Previously_watched_adaptor extends BaseAdapter {
    private Context context;
    private static java.util.ArrayList<Modelclass> ArrayList;
    LayoutInflater inflater;
    ImageView imageView;
    ProgressDialog download_dialog;
    ProgressDialog dialog;
    FolioReader folioReader = FolioReader.get();
    String file_substring;


    public Previously_watched_adaptor(Context c, ArrayList<Modelclass> arrayList)
    {
        ArrayList = arrayList;
        inflater = LayoutInflater.from(c);
        context =c;
    }

    public int getCount() {
        try {
            return ArrayList.size();

        }catch(Exception e){

            return 0;

        }
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
            //ArrayList.get(position).setID();
            holder.file_image = (ImageView) convertView.findViewById(R.id.imageView);
            holder.PreviousCardView= (CardView) convertView.findViewById(R.id.PreviouslyCardView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.file_name.setText(ArrayList.get(position).getFile_name());
        holder.file_description.setText(ArrayList.get(position).getFile_description());
        holder.file_datetime.setText(ArrayList.get(position).getDatetime_of_notice());
        holder.subjectname.setText(ArrayList.get(position).getSubject_name());
        if (ArrayList.get(position).getBoolImage() == true){
            holder.file_image.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(ArrayList.get(position).getImg_of_notice())
                    .resize(300, 300)
                    .centerCrop()
                    .into(holder.file_image);
            Picasso.with(context).setLoggingEnabled(true);
        }
        else {
            holder.file_image.setVisibility(View.GONE);
        }

        holder.PreviousCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("FileName-----------", position +"------------------------------");
                String fileurl= ArrayList.get(position).getFileURL();
                Log.e("FileName-----------", fileurl +"------------------------------");

                if(fileurl.contains(".epub")){

                    file_substring = fileurl.substring(fileurl.indexOf("/Media/")+7);
                    Log.e("file_name",file_substring);

                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + file_substring);

                    if(!file.exists()){

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                            new DownloadBookFromURL().execute(fileurl);
                        }

                    }

                    else {

                        folioReader.openBook(Environment.getExternalStorageDirectory().toString() + "/" + file_substring);

                    }

                }
                else {

                    new DownloadFileFromURL().execute(fileurl);
                }

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView file_name,file_description,file_datetime,subjectname;
        ImageView file_image;
        CardView PreviousCardView;

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
            download_dialog= new ProgressDialog(context);
            download_dialog.setMessage("Fetching...");
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
            try
            {


                if (f_url[0].contains("."))
                    extension = f_url[0].substring(f_url[0].lastIndexOf("."));

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

            dialog.setMessage("Fetching ..." + progress[0]+ "%");

            Log.e("Progress - ", String.valueOf(Integer.parseInt(progress[0])));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url)
        {
            String x = Environment
                    .getExternalStorageDirectory().toString()
                    + "/watched"+extension;
            CheckFile(x);
            DismissDialog();
        }

    }

    class DownloadBookFromURL extends AsyncTask<String, String, String>
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

    private void CheckFile( String filePath)
    {
        String url = filePath;
        String dataType="";


        if (url.toString().contains(".doc") || url.toString().contains(".docx"))
        {
            dataType = "application/msword";
        }
        else if(url.toString().contains(".pdf"))
        {
            dataType = "application/pdf";
        }
        else if(url.toString().contains(".ppt") || url.toString().contains(".pptx"))
        {
            dataType =  "application/vnd.ms-powerpoint";
        }
        else if(url.toString().contains(".xls") || url.toString().contains(".xlsx"))
        {
            dataType = "application/vnd.ms-excel";
        }
        else if(url.toString().contains(".zip") || url.toString().contains(".rar"))
        {
            dataType = "application/x-wav";
        }
        else if(url.toString().contains(".jpg"))
        {
            dataType = "image/jpg";
        }
        else if(url.toString().contains(".png"))
        {
            dataType = "image/png";
        }
        else if(url.toString().contains(".mp4") || url.toString().contains(".3gp") )
        {
            dataType = "video/*";
        }
        else if(url.toString().contains(".epub"))
        {
            dataType = "application/epub";
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
        intent.setDataAndType(uri, dataType);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Application not found", Toast.LENGTH_SHORT).show();
        }

    }

    public void DismissDialog(){

        download_dialog.dismiss();
        download_dialog.cancel();

    }




}


