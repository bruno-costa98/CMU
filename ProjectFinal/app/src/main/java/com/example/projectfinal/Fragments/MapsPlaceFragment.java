package com.example.projectfinal.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectfinal.Models.Places;
import com.example.projectfinal.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsPlaceFragment extends Fragment {

    private Places places;
    private static LatLng currentPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            places = (Places) getArguments().getSerializable("p");
            currentPosition = getArguments().getParcelable("posicao");
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {

                    Object var = arg0.getTag();
                    if (!(var instanceof Places)) {
                        return null;
                    }

                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);

                    // Getting the position from the marker
                    LatLng latLng = arg0.getPosition();

                    // Getting reference to the TextView to set latitude
                    TextView name = (TextView) v.findViewById(R.id.placeName);

                    // Getting reference to the TextView to set longitude
                    TextView distance = (TextView) v.findViewById(R.id.placeDistance);

                    // Setting the latitude
                    name.setText(places.getName());

                    DistanceCalc d = new DistanceCalc();

                    Location cLocation = new Location(LocationManager.GPS_PROVIDER);
                    cLocation.setLatitude(currentPosition.latitude);
                    cLocation.setLongitude(currentPosition.longitude);

                    Location pLocation = new Location(LocationManager.GPS_PROVIDER);
                    pLocation.setLatitude(places.getGeometry().getLocation().getLat());
                    pLocation.setLongitude(places.getGeometry().getLocation().getLng());
                    // Setting the longitude
                    double dist = (double) Math.round((d.getDistance(cLocation, pLocation) * 0.001) * 100) / 100;
                    distance.setText(dist + " km");

                    // Returning the view containing InfoWindow contents
                    return v;

                }
            });

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {

                }
            });

            double minLat = Integer.MAX_VALUE;
            double maxLat = Integer.MIN_VALUE;
            double minLon = Integer.MAX_VALUE;
            double maxLon = Integer.MIN_VALUE;

            maxLat = Math.max(currentPosition.latitude, maxLat);
            minLat = Math.min(currentPosition.latitude, minLat);
            maxLat = Math.max(places.getGeometry().getLocation().getLat(), maxLat);
            minLat = Math.min(places.getGeometry().getLocation().getLat(), minLat);
            maxLon = Math.max(currentPosition.longitude, maxLon);
            minLon = Math.min(currentPosition.longitude, minLon);
            maxLon = Math.max(places.getGeometry().getLocation().getLng(), maxLon);
            minLon = Math.min(places.getGeometry().getLocation().getLng(), minLon);

            final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(maxLat, maxLon)).include(new LatLng(minLat, minLon)).build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));

            LatLng p = new LatLng(places.getGeometry().getLocation().getLat(), places.getGeometry().getLocation().getLng());
            Marker marker;
            marker = googleMap.addMarker(new MarkerOptions().position(p).title(places.getName()));
            marker.setTag(places);
            marker = googleMap.addMarker(new MarkerOptions().position(currentPosition).title("Está aqui").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}