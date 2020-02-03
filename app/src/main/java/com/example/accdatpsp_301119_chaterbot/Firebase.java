package com.example.accdatpsp_301119_chaterbot;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.accdatpsp_301119_chaterbot.constants.Cons;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Firebase {

    static DatabaseReference reff;

    public CallbackInterface callbackInterface;

    public Firebase(CallbackInterface callbackInterface){
        this.callbackInterface = callbackInterface;

    }

    /**
     * Con este metodo creamos usuarios
     * @param view la vista se le pasa para mostrar el snackbar
     */
    private void initUser(final View view) {
        //FirebaseApp.initializeApp(this);
        //EMAIL
        String email = "freckk2@gmail.com";
        //PASSWORD
        String password = "manuel1234";
        //---
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //USER
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String result;
                if (task.isSuccessful()){
                    result = "Successful";
                    //Muestro el email del usuario creado
                    Snackbar.make(view, String.format("Created user email: %s", user.getEmail()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    result = "Fail";
                    Log.d("bart", task.getException().getLocalizedMessage());
                }
                //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getMesssages(String fecha){
        reff = FirebaseDatabase.getInstance().getReference().child("messages").child(fecha);
        final ArrayList<Message> mensajes = new ArrayList<>();
        final ArrayList<HashMap<String, Object>> myMsgs = new ArrayList<>();
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()){
                    HashMap<String, Object> auxMap;
                    DataSnapshot item = items.next();
                    auxMap = (HashMap<String, Object>) item.getValue();
                    myMsgs.add(auxMap);
                }
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                /*ArrayList<HashMap<String, Object>> myMsgs = new ArrayList<>();
                Map<String, Message> map = (Map<String, Message>) dataSnapshot.getValue();

                Log.d("message", "MAP TOSTRING: " + map.toString());
                for (Map.Entry<String, Message> entry : map.entrySet()) {
                    Log.d("message", "MENSAJE: " + entry.getValue().toString());
                }

                ArrayList<Message> messages = new ArrayList<>();
                Log.d("message", "keySet AFUERA: " + map.values().toString());
                for (Message message : map.values()){
                    messages.add(message);
                }*/
                callbackInterface.setMessages(myMsgs);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(Cons.TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public static void saveMessage(Message message){
        LocalDateTime dateTime = LocalDateTime.now();
        String fecha = String.format("%s-%s-%s", String.valueOf(dateTime.getYear()), String.valueOf(dateTime.getMonth().ordinal() + 1), String.valueOf(dateTime.getDayOfMonth()));
        reff = FirebaseDatabase.getInstance().getReference().child("messages").child(fecha);
        reff.push().setValue(message);
    }

    static boolean isSuccesful = false;
    /**
     * Con este metodo comprobamos si está logueado el usuario
     */
    public static boolean initLogin(String email, String password){
        //---

        //La unica diferencia es que hacemos un singIn en vez de un createUser

        try{
         //boolean isSuccesful = false;
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //USER
                    /*
                    String result;

                    if (task.isSuccessful()){
                        result = "Successful Log In!";
                        isSuccesful = true;

                    }else{
                        result = "Fail :(";
                        Log.d("bart", task.getException().getLocalizedMessage());
                    }
                    //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                */
                    isSuccesful = task.isSuccessful();
                }

            });
        }catch (Exception e){
            Log.d("deb", e.getLocalizedMessage());
        }
        return isSuccesful;
    }

    private void init2() {
        //singleton
        //solo se crea una instancia de ese objeto gracias al getInstance y siempre es la misma
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //para ver toda la informacion de la BD o que nos diga donde esta el error
        DatabaseReference referenciaItem = database.getReference("item");
        referenciaItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v(Cons.TAG, "messages changed: " + dataSnapshot.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(Cons.TAG, "error: " + databaseError.toException());
            }
        });

        //ponerle valor a referenciaItem
        //referenciaItem.setValue("valor item");

        //crear un hijo a referenciaItem y ponerle valor
        //referenciaItem.child("uno").setValue("hola");

        //genera una nueva variable dentro de referenciaItem con el valor hola
        /*String key = referenciaItem.push().getKey();
        referenciaItem.child(key).setValue("hola 1");
        key = referenciaItem.push().getKey();
        referenciaItem.child(key).setValue("hola 2");*/

        //texto que queremos meter
        Message item = new Message("Hola buenas", "12/12/2020", "12:00", true);
        Map<String, Object> map = new HashMap<>();
        String key = referenciaItem.child("20200109").push().getKey();
        map.put("20200109/" + key, item.toMap());
        referenciaItem.updateChildren(map);

        //
        referenciaItem.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v(Cons.TAG, "task succesfull");
                } else {
                    Log.v(Cons.TAG, task.getException().toString());
                }
            }
        });
    }
}
