package com.example.projectfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectfinal.Fragments.HistoryFragment;
import com.example.projectfinal.Fragments.ItemAdapter;
import com.example.projectfinal.Fragments.MapsFragment;
import com.example.projectfinal.Fragments.MapsPlaceFragment;
import com.example.projectfinal.Fragments.PlaceAdapter;
import com.example.projectfinal.Fragments.PlacesFragment;
import com.example.projectfinal.Fragments.RegTrainerFragment;
import com.example.projectfinal.Fragments.TrainerFragment;
import com.example.projectfinal.Interfaces.ReadingData;
import com.example.projectfinal.Models.Places;
import com.example.projectfinal.Models.Treino;
import com.example.projectfinal.Retrofit.RetrofitService;
import com.example.projectfinal.ViewModels.TreinoViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;


public class PrincipalActivity extends AppCompatActivity implements
        NavigationBarView.OnItemSelectedListener, PlaceAdapter.PlaceAdapterComunication , SensorEventListener {

//    private TextView steps;
    private SensorManager sensorManager;
    private Boolean running = false;
    private FirebaseAuth mAuth;
    private MapsFragment mapsFragment;
    private HistoryFragment historyFragment;
    private FragmentManager fragmentManager;
    private BottomNavigationView navigationView;
    private TrainerFragment trainerFragment;
    private static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient mFusedLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    double latitude, longitude;
    private TreinoViewModel treinoViewModel;
    private Treino treino;
    private ItemAdapter itemAdapter;
    private static boolean permitiuSensor = false;

    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    private int currentSteps;

    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_main);

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();

        treinoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory((Application)
                this.getApplicationContext())).get(TreinoViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SportZ");
        setSupportActionBar(toolbar);

//        steps = findViewById(R.id.stepsTextView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION},120);
        }else{
            permitiuSensor = true;
        }


        navigationView = findViewById(R.id.navigation_bar);
        navigationView.setSelectedItemId(R.id.trainer);

//        mapsFragment = new MapsFragment();
//        fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragmentContainerPrin, mapsFragment);
//        fragmentTransaction.commit();

        loadData();
//        stopRun();
        currentSteps = 0;
        sendDataToFragment();

        navigationView.setOnItemSelectedListener(this);

        if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocation.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude= location.getLongitude();

//                                Toast.makeText(PrincipalActivity.this, "Localização conseguida", Toast.LENGTH_SHORT).show();
                            } else {
                                locationRequest = LocationRequest.create();
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationRequest.setInterval(20 * 1000);

                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
//                                        if (locationResult == null) {
//                                            return;
//                                        }
                                        for (Location location : locationResult.getLocations()) {
//                                            if (location != null) {
                                                latitude = location.getLatitude();
                                                longitude = location.getLongitude();
//                                            }
                                        }
                                    }
                                };
                                mFusedLocation.requestLocationUpdates(locationRequest, locationCallback, null);
//                                Toast.makeText(PrincipalActivity.this, "A localização é a mesma de antes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });}
        else {
            lastLocation();
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapsFragment = new MapsFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerPrin, mapsFragment);
                fragmentTransaction.commit();
            }
        }, 700);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.the_toolbar, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout){
            logOut();
        }

        return super.onOptionsItemSelected(item);
    }


    private void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.trainer) {
            toMapsFragment();
            return true;
        } else if (item.getItemId() == R.id.history) {
            toHistoryFragment();
            return true;
        } else if(item.getItemId() == R.id.addTrainer) {
            toRegTrainer();
            return true;
        } else if(item.getItemId()== R.id.places){
            toPlacesFragment();
            return true;
        } else {
            return true;
        }
    }

    public void toTrainerFragment(){
        trainerFragment = new TrainerFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerPrin, trainerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void toHistoryFragment(){
        HistoryFragment historyFragment;
        historyFragment = new HistoryFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerPrin, historyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void toRegTrainer(){
        RegTrainerFragment regTrainerFragment;
        regTrainerFragment = new RegTrainerFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerPrin, regTrainerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void save(String description, String distance, String time){
        String distancia = distance + " km";
        String tempo = time;
        treino = new Treino(description, distancia, tempo, mAuth.getUid());
        treinoViewModel.insertTreino(treino);

        Toast.makeText(PrincipalActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    public void toMapsFragment(){
        MapsFragment mapsFragment;
        mapsFragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", latitude);
        bundle.putDouble("lng", longitude);
        mapsFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainerPrin, mapsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void toPlacesFragment(){
        PlacesFragment placesFragment;
        placesFragment = new PlacesFragment();
        Bundle b = new Bundle();
        LatLng place;
        b.putParcelable("place", place = new LatLng(latitude,longitude));
//        b.putDouble("long", longitude);
        placesFragment.setArguments(b);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerPrin, placesFragment);
        fragmentTransaction.commit();
    }


    public void lastLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions();
            return;
        }
    }

    public void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    @Override
    public void sendPlace(Places places) {
        Places temp = places;

        MapsPlaceFragment m = new MapsPlaceFragment();
        Bundle b = new Bundle();
        b.putSerializable("p", places);
        LatLng place;
        b.putParcelable("posicao", place = new LatLng(latitude,longitude));
        m.setArguments(b);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerPrin, m);
        fragmentTransaction.commit();
    }




    @Override
    protected void onResume() {
        super.onResume();

        if (permitiuSensor) {
            Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

            if (stepSensor == null) {
                Toast.makeText(this,"Não foi detetado sensor", Toast.LENGTH_SHORT).show();
            } else {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        running = false;
        //sensor.unregisterListener();
    }

    public void startRun() {
        running = true;
    }

    public void stopRun() {
        running = false;
        currentSteps = 0;
        previousTotalSteps = totalSteps;
        sendDataToFragment();
        saveData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key1", previousTotalSteps);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        float savedNumber = sharedPreferences.getFloat("key1", 0f);
        previousTotalSteps = savedNumber;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
         if (running){
             totalSteps = event.values[0];
             currentSteps = (Math.round(totalSteps) - Math.round(previousTotalSteps));
             Log.d("passos", String.valueOf(currentSteps));
             sendDataToFragment();
         }
    }

    public void sendDataToFragment(){
        if(mapsFragment != null){
            ((ReadingData)mapsFragment).passData(String.valueOf(currentSteps));
        }
        else
            Log.e("Error", "Fragment is null");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


}

