package com.example.projectfinal.ViewModels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projectfinal.DataBases.TreinoDao;
import com.example.projectfinal.DataBases.TreinoDatabase;
import com.example.projectfinal.Models.Treino;

import java.util.List;

public class TreinoViewModel extends AndroidViewModel {

    public String TAG = "treinoviewmodel";
    private TreinoDao treinoDao;
    private TreinoDatabase treinoDatabase;
    private LiveData<List<Treino>> mTreinos;

    public TreinoViewModel(Application application){
        super(application);
        treinoDatabase = TreinoDatabase.getDatabase(application);
        treinoDao = treinoDatabase.treinoDao();
        mTreinos = treinoDao.getAllTreinos();
    }

    public void delete(Treino treino) {
        new DeleteAsyncTask(treinoDao).execute(treino);
    }

    public void insertTreino(Treino treino){
        new insertAsyncTask(treinoDao).execute(treino);
        Log.d("insert", "inseriu" + treino.distance + treino.time);
    }

   public LiveData<List<Treino>> getAllTreinos(){
        return mTreinos;
   }

    public class insertAsyncTask extends AsyncTask<Treino, Void, Void> {
        TreinoDao treinoDao;
        public insertAsyncTask(TreinoDao mTreinoDao){
            this.treinoDao = mTreinoDao;
        }
        @Override
        protected Void doInBackground(Treino... treinos) {
             treinoDao.insertTreino(treinos[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Treino, Void, Void> {
        TreinoDao treinoDao;
        public DeleteAsyncTask(TreinoDao mTreinoDao) {
            this.treinoDao = mTreinoDao;
        }

        @Override
        protected Void doInBackground(Treino... treinos) {
            treinoDao.delete(treinos[0]);
            return null;
        }
    }
}
