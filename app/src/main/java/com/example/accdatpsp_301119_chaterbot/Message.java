package com.example.accdatpsp_301119_chaterbot;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Message {

    private String mensaje;
    private String fecha;
    private String hora;
    private boolean isBot;

    public Message(String mensaje, String fecha, String hora, boolean isBot) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.hora = hora;
        this.isBot = isBot;
    }

    public Message() {
        this("", "", "", false);
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mensaje='" + mensaje + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", isBot=" + isBot +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message", mensaje);
        result.put("fecha", fecha);
        result.put("hora", hora);
        result.put("isBot", isBot);
        return result;
    }


    public static ArrayList<Message> toMessage(Map<String, Object> map){
        ArrayList<Message> messages = new ArrayList<>();
        for (Object object: map.values()) {
            //messages.add(message);
            //Log.d("message", "MEssage: " + message.toString());
        }
        return messages;
    }
    public static Message toMessage(Object o){

    }
}
