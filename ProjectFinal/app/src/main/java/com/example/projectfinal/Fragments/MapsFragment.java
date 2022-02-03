package com.example.projectfinal.Fragments;

import static android.content.Context.LOCATION_SERVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projectfinal.Interfaces.PlacesApi;
import com.example.projectfinal.MainActivity;
import com.example.projectfinal.Models.Coordenada;
import com.example.projectfinal.Models.Places;
import com.example.projectfinal.Models.Results;
import com.example.projectfinal.PrincipalActivity;
import com.example.projectfinal.R;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public LocationManager locationManager;
//    Location location;
//    LatLng CENTER = null;

    private String mParam1;
    private String mParam2;

    private double lat;
    private double lng;

    private double oLat;
    private double oLng;

    private LatLng myPosition;
    private LatLng oldPosition;

    private LocationRequest locationRequest;

    private SupportMapFragment mapFragment;

//    private MapView map;

    public Context context;
    TextView textView;
    private GoogleMap googleMap;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            lat = getArguments().getDouble("lat");
            lng = getArguments().getDouble("lng");
        }

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
    }

//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        /**
//         * Manipulates the map once available.
//         * This callback is triggered when the map is ready to be used.
//         * This is where we can add markers or lines, add listeners or move the camera.
//         * In this case, we just add a marker near Sydney, Australia.
//         * If Google Play services is not installed on the device, the user will be prompted to
//         * install it inside the SupportMapFragment. This method will only be triggered once the
//         * user has installed Google Play services and returned to the app.
//         */
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            LatLng sydney = new LatLng(50, 120);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        }
//    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        textView = view.findViewById(R.id.distanceTextView);

        mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

                        Criteria criteria = new Criteria();
                        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                        String provider = locationManager.getBestProvider(criteria, true);

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        Location location = locationManager.getLastKnownLocation(provider);
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        myPosition = new LatLng(latitude, longitude);

//                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                myPosition, 15
                        ));

                        googleMap.addMarker(new MarkerOptions().position(myPosition).title("Marker"));


//

//
//                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

//        map = view.findViewById(R.id.map);
        Button start = view.findViewById(R.id.startButton);
        Button end = view.findViewById(R.id.endButton);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPosition = myPosition;
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        map.onCreate(savedInstanceState);
//
//        map.onResume();// needed to get the map to display immediately
//
//        try {
//            MapsInitializer.initialize(getActivity());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        map.getMapAsync(new OnMapReadyCallback() {
//            @SuppressLint("MissingPermission")
//            @Override
//            public void onMapReady(@NonNull GoogleMap mMap) {
//                googleMap = mMap;
//
//                locationManager = ((LocationManager) getActivity()
//                        .getSystemService(Context.LOCATION_SERVICE));
//
//                Boolean localBoolean = Boolean.valueOf(locationManager
//                        .isProviderEnabled("network"));
//
//                if (localBoolean.booleanValue()) {
//
//                    CENTER = new LatLng(lat, lng);
//
//                } else {
//
//                }
//
//                googleMap.setMyLocationEnabled(true);
//
//
//                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(lat, lng);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        });
        return view;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        map.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        map.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        map.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        map.onLowMemory();
//    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
//    }
}