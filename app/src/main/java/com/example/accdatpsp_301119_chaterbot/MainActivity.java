package com.example.accdatpsp_301119_chaterbot;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.accdatpsp_301119_chaterbot.api.ChatterBot;
import com.example.accdatpsp_301119_chaterbot.api.ChatterBotFactory;
import com.example.accdatpsp_301119_chaterbot.api.ChatterBotSession;
import com.example.accdatpsp_301119_chaterbot.api.ChatterBotType;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//https://github.com/pierredavidbelanger/chatter-bot-api

public class MainActivity extends AppCompatActivity {

    private ChatterBot bot;
    private ChatterBotFactory factory;
    private ChatterBotSession botSession;

    TTS tts;
    ImageView ivTalk;
    String auxiliar;

    ArrayList<String> result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        ivTalk = findViewById(R.id.ivTalk);
        Glide.with(this)
                .load(R.drawable.mic)
                .override(500, 500)
                .into(ivTalk);

        ivTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Say Something...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if(intent.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(intent, 5);
                }
                else {
                    Toast.makeText(v.getContext(),"Your Device Doesn't Support Speech Intent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==5) {
            if(resultCode==RESULT_OK && data!=null) {
                result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                new Translate().execute("es", result.get(0), "en");

            }
        }
    }

    private void init() {
        tts = new TTS(getApplicationContext());
        if(startBot()) {

        }
    }

    private String chat(final String text) {
        String response;
        try {
            response = botSession.think(text);
        } catch (final Exception e) {
            response = "";
        }

        Log.v("coco", response);
        //traducir("en", response, "es");
        return response;
    }

    private boolean startBot() {
        boolean result = true;
        String initialMessage;
        factory = new ChatterBotFactory();
        try {
            bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            botSession = bot.createSession();
            //initialMessage = "conectado" + "\n";
        } catch(Exception e) {
            //initialMessage = "ERROR" + "\n" + " " + e.toString();
            result = false;
        }
        return result;
    }

    public void traducir2(String... params){
        new Translate2().execute(params);
    }

    class Translate extends AsyncTask<String, String, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            RestClient r = new RestClient();

            HashMap<String, String> httpBodyParams;
            httpBodyParams = new HashMap<>();
            httpBodyParams.put("fromLang", strings[0]);
            httpBodyParams.put("text", strings[1]);
            httpBodyParams.put("to", strings[2]);


            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : httpBodyParams.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");
                try {
                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result.append("=");
                try {
                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            String parameters = result.toString();



            String e = r.postHttp("https://www.bing.com/ttranslatev3?isVertical=1&&IG=9AB86C10F77B448D932E5D5DB4E982F1&IID=translator.5026.3", parameters);
            Log.v("holiii", "Respuesta: " + parameters);
            String sub = e.substring(e.indexOf("\"text\":\""), e.indexOf("\"to\":\""));
            auxiliar = sub.substring(8).replace("\"", "").replace(",", "");


            traducir2("en", chat(auxiliar), "es");

            return null;
        }

    }

    class Translate2 extends AsyncTask<String, String, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            RestClient r = new RestClient();

            HashMap<String, String> httpBodyParams;
            httpBodyParams = new HashMap<>();
            httpBodyParams.put("fromLang", strings[0]);
            httpBodyParams.put("text", strings[1]);
            httpBodyParams.put("to", strings[2]);


            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : httpBodyParams.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");
                try {
                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result.append("=");
                try {
                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            String parameters = result.toString();


            String e = r.postHttp("https://www.bing.com/ttranslatev3?isVertical=1&&IG=9AB86C10F77B448D932E5D5DB4E982F1&IID=translator.5026.3", parameters);
            String sub = e.substring(e.indexOf("\"text\":\""), e.indexOf("\"to\":\""));
            auxiliar = sub.substring(8).replace("\"", "").replace(",", "");
            tts.sayHello(auxiliar);

            return null;
        }

    }


}