package com.example.projectfinal.Models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "treinos")
public class Treino  {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    @ColumnInfo(name = "descricao")
    public String type;
    @NonNull
    @ColumnInfo(name = "distancia")
    public String  distance;
    @NonNull
    @ColumnInfo(name = "tempo")
    public String time;

    public Treino(){

    }

    public Treino(String descrcao, String distancia, String tempo) {
        this.id = id;
        this.type = descrcao;
        this.distance = distancia;
        this.time = tempo;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getDistance() {
        return distance;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return " Treino: " +
                "Descrição:  " + type + '\'' +
                "Distancia:  " + distance + '\'' +
                "Tempo: " + time + '\'';
    }
}
