package com.example.projectfinal.Fragments;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projectfinal.MainActivity;
import com.example.projectfinal.R;


public class RegisterFragment extends Fragment {

    private Context context;

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;

    public RegisterFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mUsername = view.findViewById(R.id.username);
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.password);
        mRegister = view.findViewById(R.id.register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String username = mUsername.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    mUsername.setError("Email required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password required");
                    return;
                }

                ((MainActivity) context).register(mUsername, mEmail, mPassword);

            }
        });

        return view;
    }
}
