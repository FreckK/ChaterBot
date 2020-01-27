package com.example.accdatpsp_301119_chaterbot;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBot;
import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBotFactory;
import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBotSession;
import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBotType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Repository {

    private ChatterBot bot;
    private ChatterBotFactory factory;
    private ChatterBotSession botSession;
    private TTS tts;
    private CallbackInterface callback;

    public Repository(Context context, CallbackInterface callback){
        this.callback = callback;
        initBot(context);
    }
        //Bot Methods
    private void initBot(Context context) {
            tts = new TTS(context);
            startBot();
        }

    private String chat(final String text) {
        Log.d("MIPOLLA", "MENSAJE " + text);
        String response;
        try {
            Log.d("MIPOLLA", "PENSANDO...");
            response = botSession.think(text);
            Log.d("MIPOLLA", "PENSANDO...");
        } catch (final Exception e) {
            response = "";
            Log.d("MIPOLLA", "MAL");
        }

        Log.v("coco", response);
        //traducir("en", response, "es");
        Log.d("MIPOLLA", "MENSAJE AVESTRUZ" + response);
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

        //Translate

    public void traducir2(String... params){
        Log.d("MIPOLLA", "METODO TRADUCIR2");
        new Translate2().execute(params);

    }

    public void translateToEnglish(String text) {
        new Translate().execute("es", text, "en");
    }

    class Translate extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            RestClient r = new RestClient();
            Log.d("MIPOLLA", "ACABA DE ENTRAR EN EL DOINBACKGROUND DE TRANSLATE");
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
            Log.d("MIPOLLA", "PARAMETERS:  " + parameters);


            String e = r.postHttp("https://www.bing.com/ttranslatev3?isVertical=1&&IG=9AB86C10F77B448D932E5D5DB4E982F1&IID=translator.5026.3", parameters);
            Log.v("holiii", "Respuesta: " + e);
            String sub = e.substring(e.indexOf("\"text\":\""), e.indexOf("\"to\":\""));
            String auxiliar = sub.substring(8).replace("\"", "").replace(",", "");

            Log.d("MIPOLLA", "TRADUCIDO:  " + chat(auxiliar));
            traducir2("en", chat(auxiliar), "es");

            return null;
        }

    }

    class Translate2 extends AsyncTask<String, String, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            RestClient r = new RestClient();
            Log.d("MIPOLLA", "DOINBACKGROUND TRANSLATE2:  " + strings[1]);
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
            Log.d("MIPOLLA", "PARAMETROS:  " + parameters);

            String e = r.postHttp("https://www.bing.com/ttranslatev3?isVertical=1&&IG=9AB86C10F77B448D932E5D5DB4E982F1&IID=translator.5026.3", parameters);
            String sub = e.substring(e.indexOf("\"text\":\""), e.indexOf("\"to\":\""));
            String auxiliar = sub.substring(8).replace("\"", "").replace(",", "");
            Log.d("MIPOLLA", "RESPUESTA:  " + auxiliar);
            tts.sayHello(auxiliar);

            LocalDateTime dateTime = LocalDateTime.now();
            String fecha = String.format("%h/%h/%h", dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
            String hora = String.format("%h:%h", dateTime.getHour(), dateTime.getMinute());
            Message message = new Message(auxiliar, fecha, hora, true);
            callback.setMessage(message);
            ChatAdapter.getInstance().setData(callback.messages());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ChatAdapter.getInstance().notifyDataSetChanged();
        }
    }

}
