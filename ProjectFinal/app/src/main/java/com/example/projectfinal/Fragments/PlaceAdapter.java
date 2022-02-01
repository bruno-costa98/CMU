package com.example.projectfinal.Fragments;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectfinal.Models.Places;
import com.example.projectfinal.R;

import java.util.ArrayList;
import java.util.List;


public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ItemViewHolder>{

    private ArrayList<Places> placesList;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public PlaceAdapter(Context context) {
        placesList = new ArrayList<>();
        mContext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return  new PlaceAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.place = placesList.get(position);
        holder.street.setText(placesList.get(position).getName());
        String s = placesList.get(position).getPlus_code().compound_code;
        String parts[] = s.split(" ", 2);
        holder.city.setText(parts[1]);
//        holder.pais.setText(placesList.get(position).getCountry());
    }

    public void setPlacesList(List<Places> placeList) {
        this.placesList = (ArrayList<Places>) placeList;
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView street, city;
        private Places place;
        public ItemViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            street = view.findViewById(R.id.street);
            city = view.findViewById(R.id.textCity);
//            pais = view.findViewById(R.id.textCountry);
        }
    }
}