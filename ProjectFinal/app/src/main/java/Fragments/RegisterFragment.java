package Fragments;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projectfinal.MainActivity;
import com.example.projectfinal.R;
import com.google.android.material.card.MaterialCardView;


public class RegisterFragment extends Fragment {

    private Context context;

    private EditText mUsername;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;

    public RegisterFragment() {
        // Required empty public constructor
    }

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

        View view = inflater.inflate(R.layout.activity_register, container, false);

        mUsername = view.findViewById(R.id.username);
        mName = view.findViewById(R.id.name);
        mEmail = view.findViewById(R.id.email_);
        mPassword = view.findViewById(R.id.password);
        mRegister = view.findViewById(R.id.register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ((MainActivity) context).register(mEmail, mPassword);
            }
        });

        return view;
    }
}
