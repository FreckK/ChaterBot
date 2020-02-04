package com.example.accdatpsp_301119_chaterbot.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.accdatpsp_301119_chaterbot.MainActivity;
import com.example.accdatpsp_301119_chaterbot.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

        //ViewModel
    private LoginViewModel viewModel;

        //Commons
    private EditText etMail, etPassword;
    private Button btLogin;
    private CheckBox cbPersist;

        //Constants
    private final String EMAIL = "email";
    private final String PASSWD = "password";
    private final String PERSIST = "persistence";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel =  ViewModelProviders.of(this).get(LoginViewModel.class);

        initComponents();
        initEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getBooleanExtra("logout", false)){
            deleteSharedPreferences("credentials");
        }
        if (isPersist()){
            String email = etMail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            doLogin(email, password);
        }else{
            Toast.makeText(this, "Do not persistent", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillCredentials();
    }

    @Override
    protected void onPause() {
        super.onPause();

        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        saveCredential(EMAIL, email);
        saveCredential(PASSWD, password);
    }

    private void initComponents() {
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        cbPersist = findViewById(R.id.cbPersist);
    }

    private void initEvents() {
        btLogin.setOnClickListener(this);
    }

    private void fillCredentials(){
        if ((readCredential(EMAIL) != null) && ((readCredential(PASSWD) != null))){
            etMail.setText(readCredential(EMAIL));
            etPassword.setText(readCredential(PASSWD));
        }
    }

    private void saveCredential(String key, String value){
        this.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                .edit().putString(key, value).apply();
    }

    private String readCredential(String key){
        return this.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                .getString(key, null);
    }

    private boolean isPersist(){
        return this.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                .getBoolean(PERSIST, false);
    }

    private void makePersistent(){
        this.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                .edit().putBoolean(PERSIST, true).apply();
    }

    @Override
    public void onClick(View v) {
        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        Toast.makeText(this, String.format("EMAIL: %s PASSWORD: %s", email, password), Toast.LENGTH_SHORT).show();

        if (cbPersist.isSelected() && viewModel.doLogin(email, password)){
            saveCredential(EMAIL, email);
            saveCredential(PASSWD, password);
            makePersistent();
        }
        doLogin(email, password);
    }

    private void doLogin(String email, String password){
        if(viewModel.doLogin(email, password)){
            startActivity(new Intent(Login.this, MainActivity.class));
        }else{
            Toast.makeText(this, "Credentials were wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}
