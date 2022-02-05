package com.example.projectfinal.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.projectfinal.PrincipalActivity;
import com.example.projectfinal.R;

public class RegTrainerFragment extends Fragment {

    public Context context;
    private EditText mDescricao;
    private EditText mDistancia;
    private EditText mtempoH;
    private EditText mtempoM;
    private EditText mtempoS;
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
        mtempoH = view.findViewById(R.id.tempoHoras);
        mtempoM = view.findViewById(R.id.tempoMinutos);
        mtempoS = view.findViewById(R.id.tempoSegundos);
        mSave = view.findViewById(R.id.save);
        typeOfTrainer = view.findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.TypeOfTrainer, R.layout.spinner_item);
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
                Log.e("n",mtempoM.getText().toString());
                if (mDistancia.getText().toString().equals("")) {
                    Toast.makeText(context, "Distância Inválida", Toast.LENGTH_SHORT).show();
                } else if (mtempoH.getText().toString().equals("")) {
                    Toast.makeText(context, "Horas Inválidas", Toast.LENGTH_SHORT).show();
                } else if (mtempoM.getText().toString().equals("")) {
                        Toast.makeText(context, "Minutos Inválidos", Toast.LENGTH_SHORT).show();
                } else if (Math.round(Double.parseDouble(mtempoM.getText().toString().replaceAll(",", "."))) >= 60) {
                    Toast.makeText(context, "Minutos Inválidos", Toast.LENGTH_SHORT).show();
                } else if (mtempoS.getText().toString().equals("")) {
                    Toast.makeText(context, "Segundos Inválidos", Toast.LENGTH_SHORT).show();
                } else if (Math.round(Double.parseDouble(mtempoS.getText().toString().replaceAll(",", "."))) >= 60) {
                    Toast.makeText(context, "Segundos Inválidos", Toast.LENGTH_SHORT).show();
                } else {
                    String mtempo = checkDigit(mtempoH.getText().toString())  + ":" + checkDigit(mtempoM.getText().toString()) + ":" + checkDigit(mtempoS.getText().toString());
                    ((PrincipalActivity) context).save(type, mDistancia.getText().toString(), mtempo);
                    mDistancia.getText().clear();
                    mtempoH.getText().clear();
                    mtempoM.getText().clear();
                    mtempoS.getText().clear();
                }
            }
        });

        return view;
    }

    @NonNull
    private String checkDigit(@NonNull String i) {

        String str = String.valueOf(Math.round(Double.parseDouble(i.replaceAll(",", "."))));

        if (Integer.parseInt(str) <= 9) {
            while (str.startsWith("0")) {
                str = str.substring(1);
            }
            if (str.equals("")) {
                return "00";
            }
            return "0" + str;
        } else {
            while (str.startsWith("0")) {
                str = str.substring(1);
            }
            if (str.equals("")) {
                return "00";
            }
            return str;
        }
    }

}
