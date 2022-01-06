package ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import Models.User;
import Repos.UserInfoRepository;


public class UserInfoViewModel extends AndroidViewModel {
    private final UserInfoRepository userInfoRepository;

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        userInfoRepository = new UserInfoRepository(application);
    }

    public static void registerUser(User user) {
        UserInfoRepository.registerUser(user);
    }

    public void insertUser(String email) {
        userInfoRepository.insertUserToDb(email);
    }

    public LiveData<List<User>> getAllInfo() {
        return userInfoRepository.getAllUsersInfo();
    }

    public void insertAllInfo() {
        userInfoRepository.insertAllUsers();
    }

    public LiveData<User> getUserInfo(String email) {
        return userInfoRepository.getUser(email);
    }

    public void updateUserInfo(String email, User user) {
        userInfoRepository.updateUserInfo(email, user);
    }
}
