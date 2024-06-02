package projectofinal.alternativedex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.adapter.UserAdapter;
import projectofinal.alternativedex.databinding.ActivityUsersBinding;
import projectofinal.alternativedex.fragments.ChatFragment;
import projectofinal.alternativedex.listeners.UsersListener;
import projectofinal.alternativedex.models.User;
import projectofinal.alternativedex.utilities.Constants;
import projectofinal.alternativedex.utilities.PreferenceManager;

public class UsersActivity extends AppCompatActivity implements UsersListener {
    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_users);
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUsers();
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USUARIOS)
                .get()
                .addOnCompleteListener(task -> {
                   loading(false);
                   String currentUserId = preferenceManager.getString((Constants.KEY_USUARIO_ID));
                   if(task.isSuccessful() && task.getResult() != null){
                       List<User> users = new ArrayList<>();
                       for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                           if (currentUserId.equals(queryDocumentSnapshot.getId())){
                               continue;
                           }
                           User user = new User();
                           user.name = queryDocumentSnapshot.getString(Constants.KEY_NOMBRE);
                           user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                           user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGEN);
                           user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                           user.id = queryDocumentSnapshot.getId();
                           users.add(user);
                       }
                       if(users.size() > 0){
                           UserAdapter userAdapter = new UserAdapter(users, this);
                           binding.usersRecyclerView.setAdapter(userAdapter);
                           binding.usersRecyclerView.setVisibility(View.VISIBLE);
                       }else {
                           showErrorMessage();
                       }
                   } else{
                       showErrorMessage();
                   }
                });
    }
    private  void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "Sin usuario disponible"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
    private void loading (Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public void onUserClicked(User user){
        Intent intent = new Intent(getApplicationContext(), ChatFragment.class);
        intent.putExtra(Constants.KEY_USUARIO, user);
        startActivity(intent);
        finish();
    }
}