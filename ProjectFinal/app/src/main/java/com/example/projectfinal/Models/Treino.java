package com.example.projectfinal.Models;

public class Treino {

    public String type;
    public float distance;
    public String time;

    public Treino(String typeOf, float runDistance, String timeOf) {
        this.distance = runDistance;
        this.type = typeOf;
        this.time = timeOf;
    }

}
