package com.example.example;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONThread extends Thread{
    private Context context;
    private  String page;
    private Handler handler = new Handler();
    private  String json = "";

    public JSONThread(Context context, String page) {
        this.context = context;
        this.page = page;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(page);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null)
                json += line + "\n";
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } finally {
            try {
                reader.close();
                streamReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    public String getJson() {
        return  json;
    }
}
