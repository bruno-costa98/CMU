package Models;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {@Index(value = {"email"}, unique = true)})
public class User implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String email;
    public String userName;


    public User(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }
}
