package com.example.projectfinal.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    private Spinner typeOfTrainer;
    private String type;

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

        mDistancia = view.findViewById(R.id.distancia);
        mtempo = view.findViewById(R.id.tempo);
        mSave = view.findViewById(R.id.save);
        typeOfTrainer = view.findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.TypeOfTrainer, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        typeOfTrainer.setAdapter(adapter);

        typeOfTrainer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PrincipalActivity) context).save(type, mDistancia, mtempo);
            }
        });

        return view;
    }

}
