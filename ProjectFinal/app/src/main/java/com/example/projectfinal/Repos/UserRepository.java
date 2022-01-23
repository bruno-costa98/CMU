package com.example.projectfinal.Repos;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.example.projectfinal.DataBases.UserDao;
import com.example.projectfinal.DataBases.UserDatabase;
import com.example.projectfinal.Models.User;
import com.example.projectfinal.WebServices.UserFirestoreService;

import java.util.List;


public class UserRepository {

    private UserDao userDao;

    static String TAG = "repo";

    public UserRepository(Application application) {
        UserDatabase db = UserDatabase.getDatabase(application);
        userDao = db.getUserDAO();
    }

    public static void registerUser(User user) {
        Log.d(TAG, "repo");
        UserFirestoreService.addUserToFirestore(user);
    }

    public void insertUser(String email) {
        UserFirestoreService.getUserFromFireStore(email, this);
    }

    public void insertAllUsers() {
        UserFirestoreService.getAllUsersInfo(this);
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void setUser(User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUser(user);
        });
    }

    public LiveData<User> getUser(String email) {
        return userDao.getUser(email);
    }

    public void updateUserInfo(String email, User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            UserFirestoreService.updateUser(email, user);
            this.insertUser(email);
        });
    }
}
