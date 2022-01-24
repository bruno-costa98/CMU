package com.example.projectfinal.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.Models.Treino;
import com.example.projectfinal.R;
import com.example.projectfinal.ViewModels.TreinoViewModel;

import java.util.List;

public class HistoryFragment extends Fragment {


    public Context context;
    private ItemAdapter itemAdapter;

    public HistoryFragment() {
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

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        TreinoViewModel treinoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory((Application) context.getApplicationContext())).get(TreinoViewModel.class);
        itemAdapter = new ItemAdapter(context);
        recyclerView.setAdapter(itemAdapter);

        treinoViewModel.getAllTreinos().observe(getViewLifecycleOwner(), new Observer<List<Treino>>() {
            @Override
            public void onChanged(List<Treino> treinos) {
                itemAdapter.setTreinoList(treinos);
                itemAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }
}