package projectofinal.alternativedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import projectofinal.alternativedex.models.Pokemon;
import projectofinal.alternativedex.models.PokemonRespuesta;
import projectofinal.alternativedex.service.PokeApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerDatos();
    }

    private void obtenerDatos(){
        PokeApiService service = retrofit.create(PokeApiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon();

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                if (response.isSuccessful()){

                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();

                    for (int i = 0; i < listaPokemon.size(); i++) {
                        Pokemon p = listaPokemon.get(i);
                        Log.i(TAG, "Pokemon:" + p.getName());
                    }

                }else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });
    }
}