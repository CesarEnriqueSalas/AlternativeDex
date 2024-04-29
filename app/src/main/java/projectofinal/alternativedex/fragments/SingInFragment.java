package projectofinal.alternativedex.fragments;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.activities.MainActivity;

public class SingInFragment extends Fragment {
    private TextView nuevaCuenta;
    private Button boton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(requireActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);
        nuevaCuenta = view.findViewById(R.id.nuevaCuenta);
        boton = view.findViewById(R.id.SingIn);
        nuevaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).loadFragment(new SingUpFragment(), false);
            }
        });

        return view;
    }




}
