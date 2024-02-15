package projectofinal.alternativedex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.models.PokemonDetalle;
import projectofinal.alternativedex.service.PokeApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            String pokemonName = intent.getStringExtra("name");
            String pokemonUrl = intent.getStringExtra("url");
            int pokemonNumber = intent.getIntExtra("numero", -1);

            System.out.println(pokemonUrl);
            System.out.println(pokemonNumber);

            TextView nameTextView = findViewById(R.id.nameTextView);
            nameTextView.setText(pokemonName.toUpperCase());

            ImageView imageView = findViewById(R.id.imageView);
            Glide.with(this)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                            + pokemonNumber + ".png")
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://pokeapi.co/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            obtenerDetallePokemon(retrofit, pokemonNumber);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtenerDetallePokemon(Retrofit retrofit, int pokemonId) {
        PokeApiService service = retrofit.create(PokeApiService.class);
        Call<PokemonDetalle> call = service.obtenerDetallePokemon(pokemonId);
        call.enqueue(new Callback<PokemonDetalle>() {
            @Override
            public void onResponse(Call<PokemonDetalle> call, Response<PokemonDetalle> response) {
                if (response.isSuccessful()) {
                    PokemonDetalle pokemonDetalle = response.body();

                    TextView textView = findViewById(R.id.weightTextnumber);
                    textView.setText(String.valueOf(pokemonDetalle.getWeight()));

                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonDetalle> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });
    }
}