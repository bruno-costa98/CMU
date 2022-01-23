package com.example.projectfinal.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectfinal.MainActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.ViewModels.UserViewModel;

public class LoginFragment extends Fragment {

    private Button registerButton;
    private Button loginButton;
    private Context context;
    private EditText email;
    private EditText pass;
    private UserViewModel userViewModel;

    public LoginFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        userViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory((Application) getActivity()
                                .getApplicationContext())).get(UserViewModel.class);

        registerButton = view.findViewById(R.id.registerButton);
        loginButton = view.findViewById(R.id.logInButton);
        email = view.findViewById(R.id.editTextEmailAddress);
        pass = view.findViewById(R.id.editTextPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String useremail = email.getText().toString();
               String password = pass.getText().toString();

                if(TextUtils.isEmpty(useremail)){
                    email.setError("Email required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    pass.setError("Password required");
                    return;
                }

               userViewModel.insertUser(useremail);
               ((MainActivity) context).login(useremail, password);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).registerPage();
            }
        });

        return view;
    }
}



