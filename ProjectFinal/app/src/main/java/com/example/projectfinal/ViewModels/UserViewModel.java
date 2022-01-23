package com.example.projectfinal.ViewModels;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projectfinal.Models.User;
import com.example.projectfinal.Repos.UserRepository;

import java.util.List;


public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    String TAG = "user_aqui";

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void registerUser(User user) {
        Log.d(TAG, "email" + user.email);
        userRepository.registerUser(user);
    }

    public void insertUser(String email) {
        userRepository.insertUser(email);
    }

    public LiveData<List<User>> getAllInfo() {
        return userRepository.getAllUsers();
    }

    public void insertAllInfo() {
        userRepository.insertAllUsers();
    }

    public LiveData<User> getUserInfo(String email) {
        return userRepository.getUser(email);
    }

    public void updateUserInfo(String email, User user) {
        userRepository.updateUserInfo(email, user);
    }
}
