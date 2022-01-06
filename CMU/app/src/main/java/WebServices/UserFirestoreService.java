package WebServices;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import Models.User;
import Repos.UserInfoRepository;


public class UserFirestoreService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addUserToFirestore(User user) {
        Map<String, Object> fireUser = new HashMap<>();
        fireUser.put("email", user.email);
        fireUser.put("userName", user.userName);
        db.collection("users")
                .document(user.email)
                .set(fireUser);
    }

    public static void getUserFromFireStore(String email, UserInfoRepository repo) {
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String email = documentSnapshot.getString("email");
                            String userName = documentSnapshot.getString("userName");

                            User user;
                            user = new User(email, userName);

                            repo.setUserFromService(user);
                        }
                    }
                });
    }


    public static void getAllUsersInfo(UserInfoRepository repo) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String email = document.getString("email");
                                String userName = document.getString("userName");
                                User user;
                                user = new User(email, userName);
                                repo.setUserFromService(user);
                            }
                        }
                    }
                });
    }

    public static void updateUser(String email, User user) {
        db.collection("users").document(email).set(user);
    }

}
