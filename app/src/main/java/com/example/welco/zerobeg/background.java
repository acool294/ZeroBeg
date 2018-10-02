package com.example.welco.zerobeg;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by welco on 02-10-2018.
 */

public class background extends AsyncTask<String,Void,String>{
    AlertDialog dialog;
    Context context;
    public background(Context context)
    {
        this.context=context;
    }
    @Override
    protected void onPreExecute() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Results");

    }

    @Override
    protected void onPostExecute(String s) {
        dialog.setMessage(s);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        String lat=strings[0];
        String lon=strings[1];

        String constr="http://192.168.0.13:8080/Code/ngoList.php";
        try
        {
            URL url= new URL(constr);
            HttpURLConnection http;
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);
            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            String data = URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(lat,"UTF-8")
                    +"&&"+URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(lon,"UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips= http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
            String line = "";
            while ((line = reader.readLine()) !=null)
            {
                result += line;

            }
            reader.close();
            ips.close();
            http.disconnect();
            return result;
        }catch (MalformedURLException e){
            result=e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }
}
