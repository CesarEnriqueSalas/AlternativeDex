package projectofinal.alternativedex.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import projectofinal.alternativedex.R;

public class TournamentsActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);

        setupToolbar();

        Intent intent = getIntent();
        String fecha = intent.getStringExtra("fecha");
        String formato = intent.getStringExtra("formato");
        String lugarEvento = intent.getStringExtra("lugarEvento");
        String nombre = intent.getStringExtra("nombre");
        String organizador = intent.getStringExtra("organizador");
        String ubicacion = intent.getStringExtra("ubicacion");
        String imagen = intent.getStringExtra("imagen");

        imageView = findViewById(R.id.tournamentImageView);

        if (imagen != null && !imagen.isEmpty()){
            setImageFromBase64(imagen);
        }

        //prueba();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarTournaments);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImageFromBase64(String base64String) {
        byte[] decodeBytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);
        imageView.setImageBitmap(bitmap);
    }

}