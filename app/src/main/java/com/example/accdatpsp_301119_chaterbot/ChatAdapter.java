package com.example.accdatpsp_301119_chaterbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.MyViewHolder>{

    private static ChatAdapter instance = null;

    private List<Message> messages;
    LayoutInflater inflater;

    private ChatAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public static ChatAdapter getInstance(Context context){
        if (instance == null){
            instance = new ChatAdapter(context);
        }
        return instance;
    }

    public static ChatAdapter getInstance(){
        return instance;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.message, parent, false);
        return new ChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (messages.get(position).isBot()){
            holder.userMessage.setVisibility(View.GONE);
            holder.tvMessageBot.setText(messages.get(position).getMensaje());
            holder.tvHoraBot.setText(messages.get(position).getHora());
        }else{
            holder.botMessage.setVisibility(View.GONE);
            holder.tvMessageUser.setText(messages.get(position).getMensaje());
            holder.tvHoraUser.setText(messages.get(position).getHora());
        }

    }

    @Override
    public int getItemCount() {
        int n = 0;
        if (messages != null){
            n = messages.size();
        }
        return n;
    }

    public void setData(List<Message> messages){
        this.messages = messages;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView botMessage;
        public TextView tvMessageBot, tvHoraBot;

        public CardView userMessage;
        public TextView tvMessageUser, tvHoraUser;

        public MyViewHolder(View v) {
            super(v);
            botMessage = v.findViewById(R.id.botMessage);
            tvMessageBot = v.findViewById(R.id.tvMessageBot);
            tvMessageUser = v.findViewById(R.id.tvMessageUser);

            userMessage = v.findViewById(R.id.userMessage);
            tvHoraBot = v.findViewById(R.id.tvHoraBot);
            tvHoraUser = v.findViewById(R.id.tvHoraUser);

        }
    }
}
