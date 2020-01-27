package com.example.accdatpsp_301119_chaterbot.conectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Connection {

    //---Atributos de clase
    private Context context;
    private final String DEBUG = "connection_tag";

    /**
     * @param context
     * Le pasamos el contexto por que el getSystemService lo necesita
     * para realizar la accion. (Es un metodo de un objeto context basicamente)
     */
    public Connection(Context context){
        this.context = context;
    }

    /**
     * Sabemos si tenemos algo conectado
     * @return true si tenemos el wifi o los datos moviles activados
     */
    public boolean isActiveConnection(){
        return this.isMobileData() || this.isWifi();
    }
    /**
     * Devolvemos true o false si tenemos el wifi activo y conectado
     * @return true si esta el wifi conectado
     */
    public boolean isWifi() {
        boolean response = false;
        ConnectivityManager gesCon = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (gesCon != null){
            NetworkInfo redwifi = gesCon.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (redwifi != null)
                //Esto me dice si el movil dispone de una conexion wifi o no (gilipollez así que no lo vamos a usar)
                //response = redwifi.isAvailable();
                //Con este preguntamos si el movil dispone de wifi (si esta activo o conectado)
                response = redwifi.getState() == NetworkInfo.State.CONNECTED;
        }
        return response;
    }

    /**
     *  Este metodo nos devuelve true si tenemos los datos moviles activo y conectado
     * @return true si los datos conectados
     */
    public boolean isMobileData() {
        boolean response = false;
        ConnectivityManager gesCon = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (gesCon != null){
            NetworkInfo mobileData = gesCon.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileData != null){
                //Hacemos la accion si el dispositivo dispone de datos moviles
                if(mobileData.isAvailable()){
                    //Decimos true si tenemos conectados los datos moviles
                    response = mobileData.getState() == NetworkInfo.State.CONNECTED;
                }

            }
        }
        return response;
    }

    /**
     * Con este metodo comprobamos si tenemos conexion a internet haciendo ping a la url deseada
     * @param url
     * @return true si tenemos conexion
     */
    public boolean isConnected(String url){
        if(url == null || url.isEmpty()){
            url = "https://www.google.com";
        }
        final int TIEMPO_CONEXION = 2000;
        boolean conectado = false;
        try{
            HttpsURLConnection conexionHttps = (HttpsURLConnection) (new URL(url).openConnection());
            conexionHttps.setRequestProperty("User-Agent", "ConnectionTest");
            conexionHttps.setRequestProperty("Connection", "close");
            conexionHttps.setConnectTimeout(TIEMPO_CONEXION);
            conexionHttps.setReadTimeout(TIEMPO_CONEXION);
            conexionHttps.connect();
            conectado = (conexionHttps.getResponseCode() == 200);
        }catch (IOException e){
            Log.d(DEBUG, e.getLocalizedMessage());
        }
        return conectado;
    }

    /**
     * Con este metodo llamamos al isConnected(url), al pasarle null, usa la url de google
     * el gigante menos probable en caer...
     * @return true si tenemos conexion a internet
     */
    public boolean isConnected(){
        return isConnected(null);
    }
}
