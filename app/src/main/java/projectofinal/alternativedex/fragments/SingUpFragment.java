package projectofinal.alternativedex.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.activities.MainActivity;

public class SingUpFragment extends Fragment {

    private TextView iniciarSesion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_up, container, false);
        iniciarSesion = view.findViewById(R.id.iniciarSesion);
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).loadFragment(new SingInFragment(), false);
            }
        });



        return view;
    }
}