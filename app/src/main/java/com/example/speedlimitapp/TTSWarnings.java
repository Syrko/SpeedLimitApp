package com.example.speedlimitapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Simple class for tts warnings for the user
 */
public class TTSWarnings {
    private TextToSpeech tts;
    private TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status==TextToSpeech.SUCCESS)
                        tts.setLanguage(Locale.ENGLISH);
                }
            };

    public TTSWarnings(Context context){
        tts = new TextToSpeech(context, initListener);
    }

    public void SpeakSpeedWarning(){
        String message = "Attention human! You are over the designated speed limit!";
        tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
    }
}
