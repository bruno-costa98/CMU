package com.example.projectfinal.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.projectfinal.PrincipalActivity;
import com.example.projectfinal.R;

public class RegTrainerFragment extends Fragment {

    public Context context;
    private EditText mDescricao;
    private EditText mDistancia;
    private EditText mtempo;
    private Button mSave;

    public RegTrainerFragment() { }

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

        View view = inflater.inflate(R.layout.fragment_regtrainer, container, false);

        mDescricao = view.findViewById(R.id.descricao);
        mDistancia = view.findViewById(R.id.distancia);
        mtempo = view.findViewById(R.id.tempo);
        mSave = view.findViewById(R.id.save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PrincipalActivity) context).save(mDescricao, mDistancia, mtempo);
            }
        });

        return view;
    }

}
