package com.example.qianyl.networktest;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import org.xml.sax.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button)findViewById(R.id.send_request);
        responseText = (TextView)findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("MainActivity","onClick");
        if (v.getId() == R.id.send_request){
            //sendRequestWithHttpURLConnection();//lenovo 4.0 can't use
            sendRequestWithOkHttp();//lenovo 4.0 can only use this request way
        }
    }
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("MainActivity","sendRequestWithOkHttp");
                    OkHttpClient client = new OkHttpClient();
                    Log.d("MainActivity","1");
                    Request request = new Request.Builder().url("http://192.168.56.1/get_data.xml").build();
                    Log.d("MainActivity","2");
                    Response response = client.newCall(request).execute();
                    Log.d("MainActivity","3");
                    String responseData = response.body().string();
                    Log.d("MainActivity","4");
                    //showResponse(responseData);
                    //parseXMLWithPull(responseData);
                    parseXMLWithSAX(responseData);
                    Log.d("MainActivity","5");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseXMLWithSAX(String xmlData){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void parseXMLWithPull(String xmlData){
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity", "id is" + id);
                            Log.d("MainActivity", "name is" + name);
                            Log.d("MainActivity", "version is" + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendRequestWithHttpURLConnection(){
        Log.d("MainActivity","sendRequestWithHttpURLConnection");
        //start a thread to request the internet
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity","New Thread");
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    Log.d("MainActivity","1");
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    Log.d("MainActivity","2");
                    connection.setRequestMethod("GET");
                    Log.d("MainActivity","3");
                    connection.setConnectTimeout(8000);
                    Log.d("MainActivity","4");
                    connection.setReadTimeout(8000);
                    Log.d("MainActivity","5");
                    Log.d("MainActivity",connection.getResponseMessage().toString());
                    if (connection.getResponseCode() == 200){
                        Log.d("MainActivity","connect success");
                    }
                    InputStream in = connection.getInputStream();
                    Log.d("MainActivity","6");
                    // read the information from the stream
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        Log.d("MainActivity","read response");
                        response.append(line);
                    }
                    Log.d("MainActivity","start showResponse");
                    showResponse(response.toString());
                }catch (Exception e){
                    Log.d("MainActivity","new url false");
                    e.printStackTrace();
                }finally {
                    if (reader != null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        Log.d("MainActivity","showResponse");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity","UiThread run");
                //show the result to the UI
                responseText.setText(response);
            }
        });
    }
}