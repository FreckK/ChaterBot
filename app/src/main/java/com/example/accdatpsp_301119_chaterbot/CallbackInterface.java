package com.example.accdatpsp_301119_chaterbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface CallbackInterface {
    List<Message> messages();
    void setMessage(Message message);

    void setMessages(ArrayList<HashMap<String, Object>> messages);
}
