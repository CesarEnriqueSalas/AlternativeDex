package projectofinal.alternativedex.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.util.Base64;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;

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
        TextView eventoNombre = findViewById(R.id.textEvent);
        TextView eventoFecha = findViewById(R.id.textDate);
        TextView eventoFormato = findViewById(R.id.textFormat);
        TextView eventoLugar = findViewById(R.id.textLocation);
        TextView eventoUbicacion = findViewById(R.id.textUbication);
        TextView eventoOrganizador = findViewById(R.id.textOrganizer);

        eventoNombre.setText(nombre);
        eventoFecha.setText(fecha);
        eventoFormato.setText(formato);
        eventoLugar.setText(lugarEvento);
        eventoUbicacion.setText(ubicacion);
        eventoOrganizador.setText(organizador);

        if (imagen != null && !imagen.isEmpty()){
            setImageFromBase64(imagen);
        }



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

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 400;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}