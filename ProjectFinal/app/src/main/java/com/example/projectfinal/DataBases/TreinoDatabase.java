package com.example.projectfinal.DataBases;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projectfinal.Models.Treino;
import com.example.projectfinal.Models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Treino.class}, version = 2)
public abstract class TreinoDatabase extends RoomDatabase {

    public abstract TreinoDao treinoDao();
    private static volatile TreinoDatabase INSTANCE;

    public static TreinoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TreinoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TreinoDatabase.class, "treinosdatabase")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}




