package projectofinal.alternativedex.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.signedness.qual.SignednessGlb;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.activities.MainActivity;
import projectofinal.alternativedex.utilities.Constants;
import projectofinal.alternativedex.utilities.PreferenceManager;

public class SingUpFragment extends Fragment {

    private TextView iniciarSesion;
    private String encodedImage;
    private Button crearCuenta;
    private EditText nombre;
    private EditText email;
    private EditText contrasena;
    private EditText confirmarContrasena;
    private FrameLayout layoutImage;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_up, container, false);
        preferenceManager = new PreferenceManager(getContext().getApplicationContext());
        iniciarSesion = view.findViewById(R.id.iniciarSesion);
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).loadFragment(new SingInFragment(), false);
            }
        });

        // Inicializar los EditText aquí
        nombre = view.findViewById(R.id.nombre);
        email = view.findViewById(R.id.email);
        contrasena = view.findViewById(R.id.contrasena);
        confirmarContrasena = view.findViewById(R.id.confirmarContrasena);

        crearCuenta = view.findViewById(R.id.SingUp);
        crearCuenta.setOnClickListener(v -> {
            if (isValidSignUpDetails(nombre.getText().toString(), email.getText().toString(),
                    contrasena.getText().toString(), confirmarContrasena.getText().toString(),
                    encodedImage)) {
                signUp(nombre.getText().toString(), email.getText().toString(),
                        contrasena.getText().toString(), encodedImage);
            }
        });
        layoutImage = view.findViewById(R.id.layoutImage);
        layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        return view;
    }
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
    private void signUp(String name, String email, String password, String encodedImage){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NOMBRE, name);
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_CONTRASENA, password);
        user.put(Constants.KEY_IMAGEN, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USUARIOS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USUARIO_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NOMBRE, name);
                    preferenceManager.putString(Constants.KEY_IMAGEN, encodedImage);
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ImageView imageProfile = getActivity().findViewById(R.id.imagenPerfil);
                            imageProfile.setImageBitmap(bitmap);

                            TextView textAddImage = getActivity().findViewById(R.id.textoAnadirImagen);
                            textAddImage.setVisibility(View.GONE);

                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    private Boolean isValidSignUpDetails(String name, String email, String password, String confirmPassword, String encodedImage){
        if(encodedImage == null){
            showToast("Selecciona imagen de perfil");
            return false;
        } else if(name.trim().isEmpty()) {
            showToast("Introduce el nombre");
            return false;
        } else if (email.trim().isEmpty()) {
            showToast("Introduce el email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Introduce un email válido");
            return false;
        } else if (password.trim().isEmpty()) {
            showToast("Introduce una contraseña");
            return false;
        } else if (confirmPassword.trim().isEmpty()) {
            showToast("Introduce una contraseña");
            return false;
        } else if (!password.equals(confirmPassword)) {
            showToast("La contraseña y la confirmación de contraseña deben ser iguales");
            return false;
        } else{
            return true;
        }
    }
    private void loading(Boolean isLoading) {
        Button signUpButton = getView().findViewById(R.id.SingUp);
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (isLoading) {
            signUpButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
        }
    }

}