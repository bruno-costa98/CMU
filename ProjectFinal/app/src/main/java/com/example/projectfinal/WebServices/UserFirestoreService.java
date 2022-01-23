package com.example.projectfinal.WebServices;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projectfinal.Models.User;
import com.example.projectfinal.Repos.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class UserFirestoreService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String TAG = "firestore";
    static String TAG2 = "sucesso";
    static String TAG3 = "fail";

    public static void addUserToFirestore(User user) {

        Map<String, Object> user_firebase = new HashMap<>();
        user_firebase.put("email", user.email);
        user_firebase.put("userName", user.userName);

        db.collection("users").
                document(user.email).
                set(user_firebase).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG2, "Sucesso");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Log.w(TAG3, "FAIL") ;
            }
        });

    }

    public static void getUserFromFireStore(String email, UserRepository repo) {
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

                            repo.setUser(user);
                        }
                    }
                });
    }


    public static void getAllUsersInfo(UserRepository repo) {
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
                                repo.setUser(user);
                            }
                        }
                    }
                });
    }

    public static void updateUser(String email, User user) {
        db.collection("users").document(email).set(user);
    }

}
