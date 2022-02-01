package com.example.projectfinal.DataBases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.projectfinal.Models.Treino;

import java.util.List;


@Dao
public interface TreinoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTreino(Treino... treino);

    @Query("SELECT * FROM treinos")
    LiveData<List<Treino>> getAllTreinos();

    @Delete
    int delete(Treino treino);
}
