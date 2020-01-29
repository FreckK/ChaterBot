package com.example.accdatpsp_301119_chaterbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Message {

    private String mensaje;
    private String fecha;
    private String hora;
    private boolean bot;

    public Message(String mensaje, String fecha, String hora, boolean bot) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.hora = hora;
        this.bot = bot;
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
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mensaje='" + mensaje + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", bot=" + bot +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message", mensaje);
        result.put("fecha", fecha);
        result.put("hora", hora);
        result.put("bot", bot);
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
}
