package com.example.projectfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.projectfinal.Fragments.HistoryFragment;
import com.example.projectfinal.Fragments.ItemAdapter;
import com.example.projectfinal.Fragments.MapsFragment;
import com.example.projectfinal.Fragments.RegTrainerFragment;
import com.example.projectfinal.Fragments.TrainerFragment;
import com.example.projectfinal.Models.Treino;
import com.example.projectfinal.ViewModels.TreinoViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity implements
        NavigationBarView.OnItemSelectedListener{ //implements SensorEventListener {

    private TextView steps;
    private SensorManager sensor;
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
    public double latitude, longitude;
    private TreinoViewModel treinoViewModel;
    private Treino treino;
    private ItemAdapter itemAdapter;

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

        navigationView = findViewById(R.id.navigation_bar);
        navigationView.setSelectedItemId(R.id.trainer);

        mapsFragment = new MapsFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerPrin, mapsFragment);
        fragmentTransaction.commit();

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
                            Toast.makeText(PrincipalActivity.this, "Localização conseguida", Toast.LENGTH_SHORT).show();
                        } else {
                            locationRequest = LocationRequest.create();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locationRequest.setInterval(20 * 1000);

                            locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    if (locationResult == null) {
                                        return;
                                    }
                                    for (Location location : locationResult.getLocations()) {
                                        if (location != null) {
                                            latitude = location.getLatitude();
                                            longitude = location.getLongitude();
                                        }
                                    }
                                }
                            };
                            mFusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                            Toast.makeText(PrincipalActivity.this, "A localização é a mesma de antes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });}
        else {
            lastLocation();
        }
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
        } else{
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

    public void save(EditText description, EditText distance, EditText time){
        String descricao = String.valueOf(description.getText());
        String distancia = String.valueOf(distance.getText());
        String tempo = String.valueOf(time.getText());
        treino = new Treino(descricao, distancia, tempo);
        treinoViewModel.insertTreino(treino);

        Toast.makeText(PrincipalActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    public void toMapsFragment(){
        MapsFragment mapsFragment;
        mapsFragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerPrin, mapsFragment);
        fragmentTransaction.addToBackStack(null);
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


    /*
    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        Sensor countSteps = sensor.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSteps != null){
            sensor.registerListener(this, countSteps, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        //sensor.unregisterListener();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
         if (running){
             steps.setText(String.valueOf(event.values[0]));
         }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
     */
}
