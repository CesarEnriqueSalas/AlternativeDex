package projectofinal.alternativedex.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private int pokemonNumber;
    private boolean isShiny = false;

    private MediaPlayer mediaPlayer;
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupToolbar();

        Intent intent = getIntent();
        if (intent != null) {
            String pokemonName = intent.getStringExtra("name");
            pokemonNumber = intent.getIntExtra("numero", -1);

            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

            if (pokemonNumber <= 151) {
                constraintLayout.setBackgroundResource(R.drawable.kanto_map2);
            } else if (pokemonNumber <= 251) {
                constraintLayout.setBackgroundResource(R.drawable.johto_map);
            } else if (pokemonNumber <= 386) {
                constraintLayout.setBackgroundResource(R.drawable.hoenn_map);
            } else if (pokemonNumber <= 493) {
                constraintLayout.setBackgroundResource(R.drawable.sinnoh_map);
            } else if (pokemonNumber <= 649) {
                constraintLayout.setBackgroundResource(R.drawable.teselia_map);
            } else if (pokemonNumber <= 721) {
                constraintLayout.setBackgroundResource(R.drawable.kalos_map);
            } else if (pokemonNumber <= 809) {
                constraintLayout.setBackgroundResource(R.drawable.alola_map);
            } else if (pokemonNumber <= 905) {
                constraintLayout.setBackgroundResource(R.drawable.galar_map);
            } else {
                constraintLayout.setBackgroundResource(R.drawable.paldea_map);
            }

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
        nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(pokemonName.toUpperCase());

        ImageView imageView = findViewById(R.id.imageView);
        loadPokemonImage(imageView, pokemonNumber);
    }

    private void loadPokemonImage(ImageView imageView, int pokemonNumber) {
        String imageUrl;
        if (isShiny) {
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/"
                    + pokemonNumber + ".png";
            nameTextView.setTextColor(Color.parseColor("#FFF000"));
            try {
                play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                    + pokemonNumber + ".png";
            stop();
            nameTextView.setTextColor(Color.parseColor("#000000"));
        }

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.pk_shiny_sound);
            mediaPlayer.setLooping(true);
            int audioDuration = mediaPlayer.getDuration();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                }
            });

            mediaPlayer.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(audioDuration);
                        stop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
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
        pokemonDetalle.convertirTiposAEspanol(); // Convertir los tipos a espanol y mayusculas

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

        valoresStats(pokemonDetalle);
    }

    private void valoresStats(PokemonDetalle pokemonDetalle) {
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

    private void formatoNombre(PokemonDetalle pokemonDetalle) {
        TextView weightTextView = findViewById(R.id.weightTextnumber);

        int peso = pokemonDetalle.getWeight();
        String pesoTexto = String.valueOf(peso);
        int longitud = pesoTexto.length();
        String pesoFormateado;
        if (longitud > 1) {
            String parteEntera = pesoTexto.substring(0, longitud - 1);
            String parteDecimal = pesoTexto.substring(longitud - 1);
            if (parteDecimal.equals("0")) {
                pesoFormateado = parteEntera + " Kg";
            } else {
                pesoFormateado = parteEntera + "," + parteDecimal + " Kg";
            }
        } else {
            pesoFormateado = pesoTexto + " Kg";
        }
        weightTextView.setText(pesoFormateado);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cambiarShiny(View view) {
        isShiny = !isShiny;
        ImageView imageView = findViewById(R.id.imageView);
        loadPokemonImage(imageView, pokemonNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }
}
