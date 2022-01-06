package DataBases;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import Models.User;


@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User... user);

    @Query("Select * From User Where email = :user_mail")
    LiveData<User> getUser(String user_mail);

    @Query("Select * From User")
    LiveData<List<User>> getAllUsers();


}
