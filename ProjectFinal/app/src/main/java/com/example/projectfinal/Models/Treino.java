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
    @NonNull
    @ColumnInfo(name = "user")
    public String uid;

    public Treino(){

    }

    public Treino(String descrcao, String distancia, String tempo, String uid) {
        this.id = id;
        this.type = descrcao;
        this.distance = distancia;
        this.time = tempo;
        this.uid = uid;
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

    @NonNull
    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return " Treino: " +
                "Descrição:  " + type + '\'' +
                "Distancia:  " + distance + '\'' +
                "Tempo: " + time + '\'' +
                "User ID: " + uid + '\'';
    }
}
