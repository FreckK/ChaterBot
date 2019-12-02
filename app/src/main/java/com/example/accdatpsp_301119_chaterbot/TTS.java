package com.example.accdatpsp_301119_chaterbot;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTS implements TextToSpeech.OnInitListener {

    private TextToSpeech mTts;

    public TTS(Context context){
        mTts = new TextToSpeech(context,
                this  // TextToSpeech.OnInitListener
        );
    }

    public void stop(){
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
    }

    public void onInit(int status) {
            int result = mTts.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
                sayHello("ERROR");
            }
        }

    public void sayHello(String hello) {
        mTts.speak(hello,
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
    }
}
