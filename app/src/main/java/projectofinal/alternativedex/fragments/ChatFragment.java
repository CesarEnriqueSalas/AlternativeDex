package projectofinal.alternativedex.fragments;

import static android.content.Intent.getIntentOld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.activities.MainActivity;
import projectofinal.alternativedex.activities.UsersActivity;
import projectofinal.alternativedex.adapter.ChatAdapter;
import projectofinal.alternativedex.databinding.FragmentChatBinding;
import projectofinal.alternativedex.models.ChatMessage;
import projectofinal.alternativedex.models.User;
import projectofinal.alternativedex.utilities.Constants;
import projectofinal.alternativedex.utilities.PreferenceManager;


public class   ChatFragment extends Fragment {
    private TextView textName;
    private ImageView imageProfile;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;
    private FragmentChatBinding binding;
    private User recieveUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentChatBinding.inflate((getLayoutInflater()));
        preferenceManager = new PreferenceManager(requireContext());
        //loadRecieverDetails();
        //init();
    }
    private void init(){
        preferenceManager = new PreferenceManager(requireActivity().getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodingString(recieveUser.image),
                preferenceManager.getString(Constants.KEY_USUARIO_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }
    private void sendMessage(){
        HashMap<String,Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USUARIO_ID));
        message.put(Constants.KEY_RECIEVER_ID, recieveUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        binding.inputMessage.setText(null);
    }
    private Bitmap getBitmapFromEncodingString (String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        textName = rootView.findViewById(R.id.textName);
        imageProfile = rootView.findViewById(R.id.imagenPerfil);
        loadUserDetails();
        getToken();
        setListeners(rootView);
        return rootView;

    }
    private void setListeners(View rootView) {
        ImageView imageSignOut = rootView.findViewById(R.id.imagenSignOut);
        imageSignOut.setOnClickListener(v -> signOut());
        //binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
        binding.fabNewChat.setOnClickListener(v-> startActivity(new Intent(requireContext(), UsersActivity.class)));
    }


    private void loadUserDetails(){
        textName.setText(preferenceManager.getString(Constants.KEY_NOMBRE));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGEN), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageProfile.setImageBitmap(bitmap);
    }
    private void showToast(String message) {
        Toast.makeText(requireActivity().getApplicationContext(),message,Toast.LENGTH_SHORT ).show();
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USUARIOS).document(
                        preferenceManager.getString(Constants.KEY_USUARIO_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }
    private void signOut(){
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USUARIOS).document(
                        preferenceManager.getString(Constants.KEY_USUARIO_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    // Cargar el fragmento SignInFragment dentro del mismo contenedor (frameLayout) en MainActivity
                    ((MainActivity) requireActivity()).loadFragment(new SingInFragment(), false);
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }
    //private void loadRecieverDetails(){
       // recieveUser = (User) getIntentOld().getSerializableExtra(Constants.KEY_USUARIO);
     //  binding.textName.setText(recieveUser.name);
   // }



}