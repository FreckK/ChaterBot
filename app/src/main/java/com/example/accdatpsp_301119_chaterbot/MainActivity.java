package com.example.accdatpsp_301119_chaterbot;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accdatpsp_301119_chaterbot.login.Login;
import com.example.accdatpsp_301119_chaterbot.settings.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

//https://github.com/pierredavidbelanger/chatter-bot-api

public class MainActivity extends AppCompatActivity {

        //ViewModel
    private MainViewModel viewModel;

        //Toolbar
    private Toolbar toolbar;
    private MenuItem mnSettings;

        //RecyclerView
    private RecyclerView rvChat;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager lManager;

        //Commons
    private FloatingActionButton btSend;
    private EditText etMessage;

        //Conversation date
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        /**
         * Si no hay fecha seleccionada con anterioridad vemos la conversacion de hoy
         *
        if (savedInstanceState.getString("date") != null){
            fecha = savedInstanceState.getString("date");
        }else{
            LocalDateTime dateTime = LocalDateTime.now();
            fecha = String.format("%h/%h/%h", dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
        }
         */
        initComponents();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Read
        this.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getString(key, null);
        //Save
        this.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit().putString(key, value).apply();
    }

    public void readSettings(){

    }

    /**
     * Aqui recibimos por el parametro messages el texto que hemos dicho por voz
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==5) {
            if(resultCode==RESULT_OK && data!=null) {
                    //Nos devuelve un arrayList con las posibles palabras dichas
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //Traducimos la frase que es mas probable
                viewModel.translateToEnglish(result.get(0));

                    //Creamos un objeto mensaje
                LocalDateTime dateTime = LocalDateTime.now();
                String fecha = String.format("%h/%h/%h", dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
                String hora = String.format("%h:%h", dateTime.getHour(), dateTime.getMinute());
                Message message = new Message(etMessage.getText().toString().trim(), fecha, hora, false);

                    //Añadimos el mensaje a la lista de mensajes
                viewModel.addMessage(message);

                    //Lo añadimos al adaptador, refrescamos el recycler y limpiamos el cuadro de texto
                adapter.setData(viewModel.getMessages());
                adapter.notifyDataSetChanged();
                etMessage.setText("");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        mnSettings = menu.findItem(R.id.mnsettings);

        mnSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return false;
            }
        });


        MenuItem mnDate = menu.findItem(R.id.mndate);
        mnDate.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showDatePicker();
                return false;
            }
        });

        MenuItem mnLogOut = menu.findItem(R.id.mnLogOut);
        mnLogOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent logOutIntent = new Intent(MainActivity.this, Login.class);
                logOutIntent.putExtra("logout", true);
                startActivity(logOutIntent);
                return false;
            }
        });

        return true;
    }

    private void initComponents(){
        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ChaterBot");
        }
        toolbar.inflateMenu(R.menu.menu);

        //RecyclerView
        rvChat = findViewById(R.id.rvChat);
        lManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(lManager);
        adapter = ChatAdapter.getInstance(getApplicationContext());
        rvChat.setAdapter(adapter);

        //Commons
        btSend = findViewById(R.id.btSend);
        etMessage = findViewById(R.id.etMessage);


    }

    private void initEvents(){
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    btSend.setImageResource(R.drawable.mic);
                }else{
                    btSend.setImageResource(R.drawable.send);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    btSend.setImageResource(R.drawable.mic);
                }
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Si no hay nada en el campo de texto el boton actua como micro
                 * Y si hay algo actua como enviar
                 */

                if (etMessage.getText().toString().length() != 0){
                    //Enviar
                        //Traducimos el mensaje al ingles
                    viewModel.translateToEnglish(etMessage.getText().toString().trim());

                        //Creamos un objeto mensaje
                    LocalDateTime dateTime = LocalDateTime.now();
                    String fecha = String.format("%h/%h/%h", dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
                    String hora = String.format("%h:%h", dateTime.getHour(), dateTime.getMinute());
                    Message message = new Message(etMessage.getText().toString().trim(), fecha, hora, false);
                    //Firebase.getMesssages(fecha);
                        //Añadimos el mensaje a la lista de mensajes
                    viewModel.addMessage(message);

                        //Lo añadimos al adaptador, refrescamos el recycler y limpiamos el cuadro de texto
                    adapter.setData(viewModel.getMessages());
                    adapter.notifyDataSetChanged();
                    etMessage.setText("");
                }else{
                    //Microfono
                        //Hacemos un intent que nos muestra el microfono para hablar
                        //(el resultado lo recogemos en onActivityResult())
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    if(intent.resolveActivity(getPackageManager())!=null) {
                        startActivityForResult(intent, 5);
                    }
                    else {
                        Toast.makeText(v.getContext(),"Your Device Doesn't Support Speech Intent", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showDatePicker(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    // +1 because January is zero
                final String selectedDate = year + "-" + (month+1) + "-" + day;
                //PEDIR A FIREBASE QUE ME DE LOS MENSAJES QUE CONTENGAN ESA FECHA
                viewModel.getMessagesFrom(selectedDate);
                Toast.makeText(MainActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.e("preference", "Pending Preference value is: " + newValue);
        return true;
    }
}