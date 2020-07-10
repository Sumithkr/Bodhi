package com.bia.bodhinew.School;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bia.bodhinew.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import androidx.core.content.FileProvider;

public class SchoolNoticeShowAdaptor extends BaseAdapter {
    Context context;
    private static ArrayList<Modelclass> ArrayList;
    LayoutInflater inflater;
    ImageView icon_for_attachment;
    ProgressDialog dialog;


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
            holder.msg_notice_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(ArrayList.get(position).getImg_of_notice()), "image/*");
                    context.startActivity(intent);
                }
            });
            icon_for_attachment = (ImageView) convertView.findViewById(R.id.icon_for_attachment);

            //holder.msg_notice_image.setImageBitmap(notice_image_main[poistion]);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content_of_notice.setText(ArrayList.get(position).getContent_of_notice());
        Log.e("content",ArrayList.get(position).getContent_of_notice());
        holder.datetime_of_notice.setText(ArrayList.get(position).getDatetime_of_notice());

        if (ArrayList.get(position).getBoolImage())
        {

            if ( ArrayList.get(position).getImg_of_notice().contains(".pdf") || ArrayList.get(position).getImg_of_notice().contains(".doc") ||ArrayList.get(position).getImg_of_notice().contains(".docx"))
            {
                holder.msg_notice_image.setVisibility(View.GONE);
                icon_for_attachment = (ImageView) convertView.findViewById(R.id.icon_for_attachment);
                icon_for_attachment.setVisibility(View.VISIBLE);
                icon_for_attachment.setBackgroundResource(R.drawable.attachment);
                icon_for_attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            if (ArrayList.get(position).getImg_of_notice().contains(".pdf")) {
                                intent.setDataAndType(Uri.parse(ArrayList.get(position).getImg_of_notice()), "application/pdf");
                            } else if (ArrayList.get(position).getImg_of_notice().contains(".doc") || ArrayList.get(position).getImg_of_notice().contains(".docx")) {

                                    String filePath = ArrayList.get(position).getImg_of_notice() ;
                                    //new DownloadFileFromURL().execute(filePath);

                                    Log.e("Flepath",filePath);
                                    File file = new File(Environment.getExternalStorageDirectory(), filePath);
                                    Log.e("file", String.valueOf(file));
                                    Uri uri = FileProvider.getUriForFile(context, "com.bia.bodhi.fileprovider", file);
                                    Log.e("uri", String.valueOf(uri));
                                    intent.setDataAndType(uri, "application/msword");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                            }
                            context.startActivity(Intent.createChooser(intent, "Open"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
                        }*/
                        String filePath = ArrayList.get(position).getImg_of_notice() ;
                        new DownloadFileFromURL().execute(filePath);
                        //CheckFile(filePath);
                    }
                });
            }

            else{
                icon_for_attachment.setVisibility(View.GONE);
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
            icon_for_attachment.setVisibility(View.GONE);
            holder.msg_notice_image.setVisibility(View.GONE);
        }

        //holder.msg_notice_image.setImageBitmap(ArrayList.get(position).getNotice_image());
        return convertView;
    }

    static class ViewHolder {
        TextView content_of_notice,datetime_of_notice;
        ImageView msg_notice_image;

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
            dialog.setMessage("Fetching ...");
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

            dialog.setMessage("Fetching Book..." + progress[0]+ "%");

            Log.e("Progress - ", String.valueOf(Integer.parseInt(progress[0])));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url)
        {



            // dismiss the dialog after the file was downloaded
            Log.e("hoighj","yaha dekho");
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