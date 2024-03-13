package projectofinal.alternativedex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

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

        setupToolbar();

        Intent intent = getIntent();
        if (intent != null) {
            String pokemonName = intent.getStringExtra("name");
            int pokemonNumber = intent.getIntExtra("numero", -1);

            setupViews(pokemonName, pokemonNumber);

            Retrofit retrofit = buildRetrofit();
            obtenerDetallePokemon(retrofit, pokemonNumber);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViews(String pokemonName, int pokemonNumber) {
        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(pokemonName.toUpperCase());

        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                        + pokemonNumber + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void obtenerDetallePokemon(Retrofit retrofit, int pokemonId) {
        PokeApiService service = retrofit.create(PokeApiService.class);
        Call<PokemonDetalle> call = service.obtenerDetallePokemon(pokemonId);
        call.enqueue(new Callback<PokemonDetalle>() {
            @Override
            public void onResponse(Call<PokemonDetalle> call, Response<PokemonDetalle> response) {
                if (response.isSuccessful()) {
                    mostrarDetallePokemon(response.body(), pokemonId);
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

    private void mostrarDetallePokemon(PokemonDetalle pokemonDetalle, int pokemonId) {
        TextView weightTextView = findViewById(R.id.weightTextnumber);
        System.out.println(pokemonDetalle.getStats().toString());
        weightTextView.setText(String.valueOf(pokemonDetalle.getWeight()));

        TextView idTextView = findViewById(R.id.pokedexTextnumber);
        idTextView.setText(String.valueOf(pokemonId));

        List<PokemonDetalle.Type> types = pokemonDetalle.getTypes();
        StringBuilder typesStringBuilder = new StringBuilder();
        for (PokemonDetalle.Type type : types) {
            typesStringBuilder.append(type.getType().getName()).append(", ");
        }

        String typesString = typesStringBuilder.toString();
        if (typesString.endsWith(", ")) {
            typesString = typesString.substring(0, typesString.length() - 2);
        }

        TextView textView = findViewById(R.id.tipoTexto);
        textView.setText(typesString.toUpperCase());

        List<PokemonDetalle.Stat> stats = pokemonDetalle.getStats();
        for (int i = 0; i < stats.size(); i++) {
            PokemonDetalle.Stat stat = stats.get(i);
            int baseStat = stat.getBaseStat();
            ProgressBar progressBar = null;
            TextView textView1 = null;
            switch (i) {
                case 0:
                    progressBar = findViewById(R.id.determinateBar1);
                    textView1 = findViewById(R.id.HPstat);
                    break;
                case 1:
                    progressBar = findViewById(R.id.determinateBar2);
                    textView1 = findViewById(R.id.atkstat);
                    break;
                case 2:
                    progressBar = findViewById(R.id.determinateBar3);
                    textView1 = findViewById(R.id.deftat);
                    break;
                case 3:
                    progressBar = findViewById(R.id.determinateBar4);
                    textView1 = findViewById(R.id.spastat);
                    break;
                case 4:
                    progressBar = findViewById(R.id.determinateBar5);
                    textView1 = findViewById(R.id.spdstat);
                    break;
                case 5:
                    progressBar = findViewById(R.id.determinateBar6);
                    textView1 = findViewById(R.id.speed);
            }
            if (progressBar != null) {
                int max = progressBar.getMax();
                textView1.setText(String.valueOf(baseStat));
                progressBar.setProgress(Math.min(baseStat, max));
            }
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
}
