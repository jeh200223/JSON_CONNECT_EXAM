package com.example.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    int type = 1;
    String json = "";
    String page = "http://127.0.0.1:8887/member.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textview1);
        Button button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream inputStream = null;
                if (type == 1) {
                    inputStream = getResources().openRawResource(R.raw.sub_info);
                    try {
                        byte[] txt = new byte[inputStream.available()];
                        inputStream.read(txt);
                        json = new String(txt);
                        textView.setText(json);
                        inputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (type == 2) {
                    try {
                        inputStream = getAssets().open("hobby.json");
                        byte[] txt = new byte[inputStream.available()];
                        inputStream.read(txt);
                        json = new String(txt);
                        textView.setText(json);
                        inputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    JSONThread thread = new JSONThread(MainActivity.this, page);
                    thread.start();
                    try {
                        thread.join();
                        json = thread.getJson();
                        textView.setText(json);
                    } catch (InterruptedException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        TextView textView1 = findViewById(R.id.textview2);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setText("");
                if (json.equals("")) {
                    Toast.makeText(MainActivity.this, "먼저 JSON 문서 받으세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (type == 1) {
                        try {
                            JSONObject root = new JSONObject(json);
                            JSONArray array = root.getJSONArray((String) root.names().get(0));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String name = object.getString((String) object.names().get(0));
                                String info = object.getString((String) object.names().get(1));
                                textView1.append("과목명 : " + name + "\n");
                                textView1.append("내용 : " + info + "\n\n");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (type == 2) {
                        try {
                            JSONObject root = new JSONObject(json);
                            for (int i = 0; i < root.length(); i++) {
                                String name = (String) root.names().get(i);
                                JSONObject object = root.getJSONObject(name);
                                String gender = object.getString((String) object.names().get(0));
                                int age = object.getInt((String) object.names().get(1));
                                JSONArray array = object.getJSONArray((String) object.names().get(2));
                                String temp = "";
                                for (int j = 0; j < array.length(); j++) {
                                    temp += array.getString(j) + ", ";
                                }
                                temp = temp.substring(0, temp.length() - 2);
                                textView1.append("이름 : " + name + "\n");
                                textView1.append("성별 : " + gender + "\n");
                                textView1.append("나이 : " + age + "\n");
                                textView1.append("취미 : " + temp + "\n\n");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONArray root = new JSONArray(json);
                            for (int i = 0; i < root.length(); i++) {
                                JSONObject object = root.getJSONObject(i);
                                String name = object.getString((String) object.names().get(0));
                                int age = object.getInt((String) object.names().get(1));
                                textView1.append("이름 : " + name + "\n");
                                textView1.append("나이 : " + age + "\n\n");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                type = 1;
                break;
            case R.id.item2:
                type = 2;
                break;
            case R.id.item3:
                type = 3;
        }
        item.setChecked(true);
        return true;
    }
}