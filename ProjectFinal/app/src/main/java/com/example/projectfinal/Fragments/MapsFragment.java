package com.example.projectfinal.Fragments;

import static android.content.Context.LOCATION_SERVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.provider.Settings;

import android.provider.SyncStateContract;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import com.example.projectfinal.R;

import com.example.projectfinal.MainActivity;
import com.example.projectfinal.Models.Coordenada;
import com.example.projectfinal.Models.Treino;
import com.example.projectfinal.PrincipalActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.ViewModels.TreinoViewModel;
import com.google.android.gms.common.internal.Constants;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOCase;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private FusedLocationProviderClient servicoLocalizacao;
    private GoogleMap mMap;

    private boolean permitiuGPS = false;
    private static Location ultimaPosicao;
    private static Location oldPosition;

    private static boolean isRunning = false;



    private double distance;


    private SupportMapFragment mapFragment;


    public Context context;
    TextView textView;
    private GoogleMap googleMap;

    private TextView timeView;
    private Button start, end;
    private int seconds = 0;
    private boolean running, wasRunning;
    private String time;
    private TreinoViewModel treinoViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        LocationManager gpsHabilitado = (LocationManager) context.getSystemService(LOCATION_SERVICE);


        if(!gpsHabilitado.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            Toast.makeText(context.getApplicationContext(), "Para este aplicativo é necessário habilitar o GPS", Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},120);
        }else{
            permitiuGPS = true;
        }


        servicoLocalizacao = LocationServices.getFusedLocationProviderClient(context);




    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        treinoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory((Application)
                context.getApplicationContext())).get(TreinoViewModel.class);
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        TextView textView = view.findViewById(R.id.distanceTextView);
        timeView = view.findViewById(R.id.timeTextView);
        start = view.findViewById(R.id.start_button);
        end = view.findViewById(R.id.endButton);
      
        if (savedInstanceState != null) {
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
        }


        if (!isRunning) {
            start.setEnabled(true);
            end.setEnabled(false);
        } else {
            start.setEnabled(false);
            end.setEnabled(true);
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        oldPosition = ultimaPosicao;
                        Log.e("l", oldPosition.getLatitude() + ", " + oldPosition.getLongitude());
                    }
                }, 1000);
                end.setEnabled(true);
                start.setEnabled(false);
                isRunning = true;
                running = true;
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recuperarPosicaoAtual();
                Log.e("l", oldPosition.getLatitude() + ", " + oldPosition.getLongitude());
                Log.e("l", ultimaPosicao.getLatitude() + ", " + ultimaPosicao.getLongitude());
                Log.e("l", ""+ultimaPosicao.distanceTo(oldPosition));

                isRunning = false;
                end.setEnabled(false);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


//                        distance = (double) Math.round((ultimaPosicao.distanceTo(oldPosition) * 0.001) * 100) / 100;
                        distance = (double) Math.round((ultimaPosicao.distanceTo(oldPosition) * 0.001) * 100) / 100;
                        textView.setText("Total distance: "+ distance);
                        start.setEnabled(true);
                        running = false;
                        time = timeView.getText().toString();
                        Treino treino = new Treino("Run", distance + "km", time);
                        treinoViewModel.insertTreino(treino);
                    }
                }, 1000);

            }
        });
      
        runTimer();

        return view;
    }

    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState
                .putInt("seconds", seconds);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);



    }

    private void recuperarPosicaoAtual() {
        try {

            if (permitiuGPS) {
                Task locationResult = servicoLocalizacao.getLastLocation();

                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            //Recupera os dados de localização da última posição
                            ultimaPosicao = (Location) task.getResult();
                            
                            //Se for um valor válido
                            if(ultimaPosicao != null){
                                //Move a câmera para o ponto recuperado e aplica um Zoom de 15 (valor padrão)
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(ultimaPosicao.getLatitude(),
                                                ultimaPosicao.getLongitude()), 15));
                            }
                        } else {
                            //Exibe um Toast se o valor que recuperou do GPS não é válido
                            Toast.makeText(context.getApplicationContext(), "Não foi possível recuperar a posição.", Toast.LENGTH_LONG).show();
                            //Escreve o erro no LogCat
                            Log.e("TESTE_GPS", "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("TESTE_GPS", e.getMessage());
        }
    }

    //Adiciona o botão para centralizar o mapa na posição atual. Esse botão é aquele parecido com um
    //alvo que fica no canto superior direito do mapa.
    private void adicionaComponentesVisuais() {
        //Se o objeto do mapa não existir, encerra o carregamento no return
        if (mMap == null) {
            return;
        }

        try {

            if (permitiuGPS) {

                mMap.setMyLocationEnabled(true);

                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);


                ultimaPosicao  = null;


                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},120);
                }
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        recuperarPosicaoAtual();
        adicionaComponentesVisuais();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    public final Double getDistance() {

        final Double[] d = new Double[1];
        Thread thread = new Thread((Runnable) new Runnable() {
            public final void run() {
                try {

                    URL url = new URL("https://api.mapbox.com/directions/v5/mapbox/walking" + '/' + oldPosition.getLongitude()+ ',' + oldPosition.getLatitude()
                            + ';' + ultimaPosicao.getLongitude() + ',' + ultimaPosicao.getLatitude() + "?geometries=geojson&access_token=pk.eyJ1IjoibWFyb3RvMTIzNDUiLCJhIjoiY2t4bmE1dm4wMjZpNDJya2p0dWRrangwbCJ9.hPFUdTzeFL2PHhBhKjvvzg");
                    Log.v("urldirection", url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    if(conn.getResponseCode() == 200) {
                        BufferedInputStream inp = new BufferedInputStream(conn.getInputStream());
                        JSONObject jsonObject = new JSONObject(IOUtils.toString((InputStream) inp, "UTF-8"));
                        JSONArray array = jsonObject.getJSONArray("routes");
                        JSONObject routes = array.getJSONObject(0);
                        JSONArray legs = routes.getJSONArray("legs");
                        JSONObject steps = legs.getJSONObject(0);
                        d[0] = steps.getDouble("distance");
                    }
                    Log.e("THREAD", String.valueOf(d[0]));
                } catch (JSONException var9) {
                    Log.e("ContentValues", var9.toString());
                } catch (IOException var10) {
                    Log.e("ContentValues", var10.toString());
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return d[0];
    }



    @Override
    public void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // caso a apliacação seja minimizada
    // mas o atleta ja estava a correr volta a iniciar o cronometro
    @Override
    public void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }


    private void runTimer()
    {

        final Handler handler
                = new Handler();

        handler.post(new Runnable() {

            @Override
            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

            //formatar o tempo
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                // alterar na textView
                timeView.setText(time);

                // se tiver a correr incrementa
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }


}