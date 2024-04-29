package projectofinal.alternativedex.fragments;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.activities.MainActivity;
import projectofinal.alternativedex.utilities.Constants;
import projectofinal.alternativedex.utilities.PreferenceManager;

public class SingInFragment extends Fragment {
    private TextView nuevaCuenta;
    private Button iniciarSesion;
    private TextView email;
    private TextView contraseña;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);
        preferenceManager = new PreferenceManager(getContext().getApplicationContext());
        //if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            //Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
            //startActivity(intent);
            //finish();
        //}
        nuevaCuenta = view.findViewById(R.id.nuevaCuenta);
        email = view.findViewById(R.id.email); // Corregido aquí
        contraseña = view.findViewById(R.id.contraseña); // Corregido aquí

        nuevaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).loadFragment(new SingUpFragment(), false);
            }
        });
        iniciarSesion = view.findViewById(R.id.SingIn);
        iniciarSesion.setOnClickListener(v ->{
            if(isValidSignInDetails()){
                signIn();
            }
        });

        return view;
    }
    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USUARIOS)
                .whereEqualTo(Constants.KEY_EMAIL,email.getText().toString())
                .whereEqualTo(Constants.KEY_CONTRASENA, contraseña.getText().toString())
                .get()
                .addOnCompleteListener(task ->{
                   if(task.isSuccessful() && task.getResult() != null
                   && task.getResult().getDocuments().size() > 0){
                       DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                       preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                       preferenceManager.putString(Constants.KEY_USUARIO_ID, documentSnapshot.getId());
                       preferenceManager.putString(Constants.KEY_NOMBRE, documentSnapshot.getString(Constants.KEY_NOMBRE));
                       preferenceManager.putString(Constants.KEY_IMAGEN, documentSnapshot.getString(Constants.KEY_IMAGEN));
                       Intent intent = new Intent(requireContext(), MainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);
                   }else{
                       loading(false);
                       showToast("No se ha podido iniciar sesion");
                   }
                });
    }
    private void loading(Boolean isLoading) {
        Button signInButton = getView().findViewById(R.id.SingIn);
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (isLoading) {
            signInButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
    private Boolean isValidSignInDetails() {
        if (email.getText().toString().trim().isEmpty()) {
            showToast("Introduce el email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast("Introduce un email válido");
            return false;
        } else if (contraseña.getText().toString().trim().isEmpty()) {
            showToast("Introduce la contraseña");
            return false;
        } else {
            return true;
        }
    }
}
