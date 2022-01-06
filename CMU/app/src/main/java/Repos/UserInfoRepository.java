package Repos;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import DataBases.UserDao;
import DataBases.UserDatabase;
import Models.User;
import WebServices.UserFirestoreService;


public class UserInfoRepository {
    private UserDao userInfoDao;

    public UserInfoRepository(Application application) {
        UserDatabase db = UserDatabase.getDatabase(application);
        userInfoDao = db.getUserDAO();
    }

    public static void registerUser(User user) {
        UserFirestoreService.addUserToFirestore(user);
    }

    public void insertUserToDb(String email) {
        UserFirestoreService.getUserFromFireStore(email, this);
    }

    public LiveData<List<User>> getAllUsersInfo() {
        return userInfoDao.getAllUsers();
    }

    public void insertAllUsers() {
        UserFirestoreService.getAllUsersInfo(this);
    }

    public void setUserFromService(User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userInfoDao.insertUser(user);
        });
    }

    public LiveData<User> getUser(String email) {
        return userInfoDao.getUser(email);
    }

    public void updateUserInfo(String email, User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            UserFirestoreService.updateUser(email, user);
            this.insertUserToDb(email);
        });
    }
}
