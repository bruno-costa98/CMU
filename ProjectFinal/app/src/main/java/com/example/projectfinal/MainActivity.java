package com.example.projectfinal;


import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.example.projectfinal.Models.User;
import com.example.projectfinal.ViewModels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.projectfinal.Fragments.LoginFragment;
import com.example.projectfinal.Fragments.RegisterFragment;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private Context context;
    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    String TAG = "here";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory((Application)
                context.getApplicationContext())).get(UserViewModel.class);

        createNotificationChannel();

        LoginFragment loginFrag = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, loginFrag);
        fragmentTransaction.commit();
    }


    public void registerPage() {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            String errorCode = ((FirebaseAuthException) task.getException())
                                    .getErrorCode();
                            Toast.makeText(context, "Erro de login!", Toast.LENGTH_SHORT).show();
                            } else {
                            toPrincipalPage();
                        }
                    }
                });
    }

    public void register(EditText mUserName, EditText mEmail, EditText mPassword) {

/*
        mAuth = FirebaseAuth.getInstance();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String userName = mUserName.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password required");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    gerarNotificacao();
                } else {
                    Toast.makeText(MainActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        }); */

        mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(),
                mPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (TextUtils.isEmpty(mEmail.getText())) {
                            mEmail.setError("Email required");
                            return;
                        } else if (TextUtils.isEmpty(mPassword.getText())){
                            mPassword.setError("Password required");
                            return;
                        } else {
                            User user = new User(mEmail.getText().toString(), mUserName.getText().toString());
                            Log.d(TAG, user.email + user.userName + user.userId);
                            userViewModel.registerUser(user);
                            mAuth.signOut();
                            fragmentManager.popBackStack();
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            gerarNotificacao();
                        }
                    }
                });
    }

    private void createNotificationChannel() {

        // Cria o canal de notificação para a API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importancia = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "Canal", importancia);
            channel.setDescription("Descricao do canal");

            // Regista o canal no sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void gerarNotificacao(){

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, PrincipalActivity.class), 0);
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this, "1");
        builder.setTicker("Texto");
        builder.setContentTitle("Bem vindo!");
        builder.setContentText("Vamos treinar?");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.running));
        builder.setContentIntent(p);

        Notification n = builder.build();
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.ic_launcher, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch(Exception e){}
    }

    public void toPrincipalPage(){
        Intent intent = new Intent(context, PrincipalActivity.class);
        startActivity(intent);
        Toast.makeText(context, "Success!",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}

