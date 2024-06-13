package projectofinal.alternativedex.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.adapter.ChatAdapter;
import projectofinal.alternativedex.models.ChatMessage;
import projectofinal.alternativedex.models.User;
import projectofinal.alternativedex.utilities.Constants;
import projectofinal.alternativedex.utilities.PreferenceManager;

public class MessageFragment extends Fragment {
    private User receiveUser;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;
    private RecyclerView chatRecyclerView;
    private TextView textName;
    private ImageView imageBack;
    private EditText inputMessage;
    private View layoutSend;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            receiveUser = (User) getArguments().getSerializable(Constants.KEY_USUARIO);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        init(rootView);
        loadReceiverDetails();
        setListeners();
        listenMessages();
        return rootView;
    }

    private void init(View rootView) {
        preferenceManager = new PreferenceManager(requireContext());
        chatMessages = new ArrayList<>();
        Bitmap userImage = (receiveUser != null && receiveUser.image != null) ? getBitmapFromEncodedString(receiveUser.image) : null;
        chatAdapter = new ChatAdapter(
                chatMessages,
                userImage,
                preferenceManager.getString(Constants.KEY_USUARIO_ID)
        );
        chatRecyclerView = rootView.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setAdapter(chatAdapter);
        textName = rootView.findViewById(R.id.textName);
        imageBack = rootView.findViewById(R.id.imageBack);
        inputMessage = rootView.findViewById(R.id.inputMessage);
        layoutSend = rootView.findViewById(R.id.layoutSend);
        progressBar = rootView.findViewById(R.id.progressBar);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        String messageText = inputMessage.getText().toString();
        if (!messageText.isEmpty()) {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USUARIO_ID));
            message.put(Constants.KEY_RECIEVER_ID, receiveUser.id);
            message.put(Constants.KEY_MESSAGE, messageText);
            message.put(Constants.KEY_TIMESTAMP, new Date());
            database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
                    .addOnSuccessListener(documentReference -> Log.d("MessageFragment", "Message sent successfully"))
                    .addOnFailureListener(e -> Log.e("MessageFragment", "Error sending message", e));
            inputMessage.setText(null);
        }
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USUARIO_ID))
                .whereEqualTo(Constants.KEY_RECIEVER_ID, receiveUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiveUser.id)
                .whereEqualTo(Constants.KEY_RECIEVER_ID, preferenceManager.getString(Constants.KEY_USUARIO_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.recieverId = documentChange.getDocument().getString(Constants.KEY_RECIEVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(count, chatMessages.size() - count);
                chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            chatRecyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    };


    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void loadReceiverDetails() {
        if (receiveUser != null) {
            textName.setText(receiveUser.name);
        }
    }

    private void setListeners() {
        imageBack.setOnClickListener(v -> requireActivity().onBackPressed());
        layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault());
        return sdf.format(date);
    }

    public static MessageFragment newInstance(User user) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_USUARIO, user);
        fragment.setArguments(args);
        return fragment;
    }
}
