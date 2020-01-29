package com.example.accdatpsp_301119_chaterbot;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBot;
import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBotFactory;
import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBotSession;
import com.example.accdatpsp_301119_chaterbot.apiBot.ChatterBotType;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel  implements CallbackInterface{

    private Repository repository;
    private static List<Message> messages = new ArrayList<>();
    private Firebase firebase;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(getApplication().getApplicationContext(), this);
        firebase = new Firebase(this);
    }


    private void poblateMessages(){
        List<Message> mensajes = new ArrayList<>();
        mensajes.add(new Message("Hola buenas", "12/12/2020", "12:00", true));
        mensajes.add(new Message("Comeme la polla", "12/12/2020", "12:01", false));
        mensajes.add(new Message("No seas grosero", "12/12/2020", "12:02", true));
        mensajes.add(new Message("Grosera tu puta madre", "12/12/2020", "12:03", false));
    }

    public void translateToEnglish(String text){
        repository.translateToEnglish(text);
    }

    public static void addMessage(Message message) {
        Firebase.saveMessage(message);
        messages.add(message);
    }

    public List<Message> getMessages(){
        return messages;
    }

    @Override
    public List<Message> messages() {
        return this.getMessages();
    }

    @Override
    public void setMessage(Message message) {
        Firebase.saveMessage(message);
        this.messages().add(message);
    }

    @Override
    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        ChatAdapter adapter = ChatAdapter.getInstance();
        adapter.setData(messages);
    }

    public void getMessagesFrom(String date){
        firebase.getMesssages(date);
    }


}
