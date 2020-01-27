package com.example.accdatpsp_301119_chaterbot.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.accdatpsp_301119_chaterbot.Firebase;

public class LoginViewModel extends AndroidViewModel {

    private Firebase firebase;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean doLogin(String email, String password){
        return Firebase.initLogin(email, password);
    }
}
