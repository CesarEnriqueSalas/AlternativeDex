package projectofinal.alternativedex.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.adapter.ListaTournamentsAdapter;
import projectofinal.alternativedex.models.Tournaments;
public class TournamentFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Tournaments> tournamentsArrayList;
    ListaTournamentsAdapter listaTournamentsAdapter;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tournament, container, false); // Inflando el diseño fragment_tournament

        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recyclerViewTournaments);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        firebaseFirestore = FirebaseFirestore.getInstance();
        tournamentsArrayList = new ArrayList<>();
        listaTournamentsAdapter = new ListaTournamentsAdapter(view.getContext(), tournamentsArrayList);

        recyclerView.setAdapter(listaTournamentsAdapter);

        EventChangeListener();

        return view;
    }

    private void EventChangeListener() {
        firebaseFirestore.collection("regionalTournaments")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                tournamentsArrayList.add(dc.getDocument().toObject(Tournaments.class));
                            }
                            listaTournamentsAdapter.notifyDataSetChanged();
                        }
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }
}