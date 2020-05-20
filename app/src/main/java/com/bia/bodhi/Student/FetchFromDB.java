package com.bia.bodhi.Student;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchFromDB extends AsyncTask<Void, Void, String>
{
    private final String urlWebService;
    public AsyncResponse delegate = null;

    public interface AsyncResponse
    {
        void processFinish(String output);
    }


    public FetchFromDB(String urlWebService , AsyncResponse delegate)
    {
        this.urlWebService = urlWebService;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Void... voids)
    {

        try
        {

            URL url = new URL(urlWebService);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;

            while ((json = bufferedReader.readLine()) != null)
            {
                sb.append(json + "\n");
            }

            return sb.toString().trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        try
        {

          delegate.processFinish(s);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
