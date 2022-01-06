package com.example.cmu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import Frags.LoginFragment;
import Frags.RegisterFragment;
import Models.User;
import ViewModels.UserInfoViewModel;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth;
    private UserInfoViewModel userInfoViewModel;

    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = getApplicationContext();

        //firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = firebaseAuth.getCurrentUser();


            LoginFragment loginFragment = new LoginFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragmentContainer, loginFragment);
            fragmentTransaction.commit();

    }


    public void register(EditText userName, EditText email, EditText password) {
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            String errorCode = ((FirebaseAuthException) task.getException())
                                    .getErrorCode();

                            switch (errorCode) {
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    email.setError("Email em uso!");
                                    Toast.makeText(context, "Este email já está em uso!",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case "ERROR_INVALID_EMAIL":
                                    email.setError("Email Inválido!");
                                    Toast.makeText(context, "Email mal formatado!",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    password.setError("Password Fraca!");
                                    Toast.makeText(context, "Password fraca! Minimo 6 caracteres.",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "Erro de registo!",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            User user = new User(email.getText().toString(), userName.getText().toString());
                            UserInfoViewModel.registerUser(user);
                            firebaseAuth.signOut();
                            fragmentManager.popBackStack();
                            Toast.makeText(context, "Registo completo!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean validateForm(EditText insertUserName,
                                EditText insertEmailField,
                                EditText insertPassField) {
        boolean valid = true;

        String userName = insertUserName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            insertEmailField.setError("Campo obrigatório!");
            valid = false;
        } else {
            insertEmailField.setError(null);
        }


        String email = insertEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            insertEmailField.setError("Campo obrigatório!");
            valid = false;
        } else {
            insertEmailField.setError(null);
        }

        String password = insertPassField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            insertPassField.setError("Campo obrigatório!");
            valid = false;
        } else {
            insertPassField.setError(null);
        }

        return valid;
    }

    public void registerRedirect() {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}