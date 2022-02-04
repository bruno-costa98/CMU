package com.example.projectfinal.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectfinal.Interfaces.PlacesApi;
import com.example.projectfinal.Models.Places;
import com.example.projectfinal.Models.Results;
import com.example.projectfinal.R;
import com.example.projectfinal.Retrofit.RetrofitService;
import com.example.projectfinal.ViewModels.TreinoViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String key = "AIzaSyAHN9Lk6OTmYy31jsSxSvhF6OCiJWl1yUg";

    private Results results;
    private Context context;

    private LatLng place;

    private PlaceAdapter adapter;

    private RetrofitService retrofitService = new RetrofitService();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public PlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlacesFragment newInstance(String param1, String param2) {
        PlacesFragment fragment = new PlacesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            place = getArguments().getParcelable("place");
            Log.e("l",place.toString().replaceAll("[()]",""));
        }

        adapter = new PlaceAdapter(context);
        
        PlacesApi placesApi = retrofitService.getRetrofit().create(PlacesApi.class);

        String location = place.latitude + "," + place.longitude;
        Call<Results> call = placesApi.getPlaces(location,5000, "gym", key);

        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                results = response.body();

                adapter.setPlacesList(results.getResults());
                adapter.notifyDataSetChanged();

                for (Places p : results.getResults()){
                    System.out.printf(p.getName());
                    Log.e("place", p.getName());
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_places, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.placeslist);
//        TreinoViewModel treinoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory((Application) context.getApplicationContext())).get(TreinoViewModel.class);

        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return v;
    }
}