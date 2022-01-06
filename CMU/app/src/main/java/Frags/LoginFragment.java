package Frags;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cmu.MainActivity;
import com.example.cmu.R;
import com.google.android.material.card.MaterialCardView;

import ViewModels.UserInfoViewModel;


public class LoginFragment extends Fragment  {

    private Context context;

    private EditText insertEmailField;
    private EditText insertPassField;
    private TextView welcomeView;
    private TextView orView;
    private Button signInButton;
    private Button registerButton;
    private MaterialCardView card;
    private UserInfoViewModel userInfoViewModel;

    public LoginFragment() {
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

        View view = inflater.inflate(R.layout.fragmentlogin, container, false);


        card = view.findViewById(R.id.materialCardView);
        welcomeView = view.findViewById(R.id.welcomeView);
        orView = view.findViewById(R.id.orView);
        insertEmailField = view.findViewById(R.id.editTextEmailAddress);
        insertPassField = view.findViewById(R.id.editTextPassword);
        signInButton = view.findViewById(R.id.logInButton);
        registerButton = view.findViewById(R.id.registerButton);

        userInfoViewModel =
                new ViewModelProvider(this,
                        new ViewModelProvider.AndroidViewModelFactory((Application) getActivity()
                                .getApplicationContext())).get(UserInfoViewModel.class);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).registerRedirect();
            }
        });

        return view;
    }
}