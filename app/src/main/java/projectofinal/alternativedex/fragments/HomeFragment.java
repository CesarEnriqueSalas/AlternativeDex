package projectofinal.alternativedex.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.adapter.ListaPokemonAdapter;
import projectofinal.alternativedex.models.Pokemon;
import projectofinal.alternativedex.models.PokemonRespuesta;
import projectofinal.alternativedex.service.PokeApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String TAG = "POKEDEX";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ListaPokemonAdapter listaPokemonAdapter;
    private int offset;
    private boolean aptoParaCargar;
    private Chip allPokemonChip, gen1PokemonChip, gen2PokemonChip, gen3PokemonChip, gen4PokemonChip, gen5PokemonChip, gen6PokemonChip, gen7PokemonChip, gen8PokemonChip, gen9PokemonChip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Encontrar las vistas después de inflar el diseño
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.buscador);

        allPokemonChip = view.findViewById(R.id.allPokemon);
        gen1PokemonChip = view.findViewById(R.id.firstGen);
        gen2PokemonChip = view.findViewById(R.id.secondGen);
        gen3PokemonChip = view.findViewById(R.id.thirdGen);
        gen4PokemonChip = view.findViewById(R.id.fourthGen);
        gen5PokemonChip = view.findViewById(R.id.fifthGen);
        gen6PokemonChip = view.findViewById(R.id.sixthGen);
        gen7PokemonChip = view.findViewById(R.id.seventhGen);
        gen8PokemonChip = view.findViewById(R.id.eighthGen);
        gen9PokemonChip = view.findViewById(R.id.ninethGen);

        listaPokemonAdapter = new ListaPokemonAdapter(getContext());

        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy > 0){
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemCount = layoutManager.getItemCount();
//                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//
//                    if(aptoParaCargar){
//                        if((visibleItemCount + pastVisibleItems) >= totalItemCount){
//                            Log.i(TAG, "Llegamos al final.");
//
//
//                            if (offset < 985) {
//                                offset += 25;
//                                obtenerDatos(offset);
//                            }
//                        }
//                    }
//                }
//            }
//        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        aptoParaCargar = true;
        offset = 0;
        obtenerDatos(offset);
        initListener();
        configureChipListeners();

        return view;
    }

    private void initListener(){
        searchView.setOnQueryTextListener(this);
    }

    private void obtenerDatos(int offset){
        PokeApiService service = retrofit.create(PokeApiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon(1025, offset);

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()){
                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();

                    for (Pokemon pokemon: listaPokemon) {
                        pokemon.setGeneration(calcularGeneracion(pokemon.getNumberPNG()));
                        System.out.println(pokemon.getNumberPNG() + " : " + pokemon.getName() + " : " + pokemon.getGeneration());
                    }

                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);
                    listaPokemonAdapter.actualizarOriginalPokemon();
                    listaPokemonAdapter.filter(searchView.getQuery().toString());
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });
    }

    private void configureChipListeners() {
        Chip[] chips = {allPokemonChip, gen1PokemonChip, gen2PokemonChip, gen3PokemonChip, gen4PokemonChip, gen5PokemonChip, gen6PokemonChip, gen7PokemonChip, gen8PokemonChip, gen9PokemonChip};
        String[] generations = {"Gen", "Gen1", "Gen2", "Gen3", "Gen4", "Gen5", "Gen6", "Gen7", "Gen8", "Gen9"};

        for (int i = 0; i < chips.length; i++) {
            Chip chip = chips[i];
            String generation = generations[i];
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (generation.equals("Gen")) {
                        listaPokemonAdapter.genAllList(generation);
                        aptoParaCargar = true;
                    } else {
                        listaPokemonAdapter.genList(generation);
                        aptoParaCargar = false;
                    }
                    resetOtherChips(chip);
                    checkAndActivateAllChip();
                }
            });
        }
    }

    private void resetOtherChips(Chip selectedChip) {
        Chip[] allChips = {allPokemonChip, gen1PokemonChip, gen2PokemonChip, gen3PokemonChip, gen4PokemonChip, gen5PokemonChip, gen6PokemonChip, gen7PokemonChip, gen8PokemonChip, gen9PokemonChip};
        for (Chip chip : allChips) {
            if (chip != selectedChip) {
                chip.setChecked(false);
            }
        }
    }

    private void checkAndActivateAllChip() {
        Chip[] allChips = {gen1PokemonChip, gen2PokemonChip, gen3PokemonChip, gen4PokemonChip, gen5PokemonChip, gen6PokemonChip, gen7PokemonChip, gen8PokemonChip, gen9PokemonChip};
        boolean allUnchecked = true;

        for (Chip chip : allChips) {
            if (chip.isChecked()) {
                allUnchecked = false;
                break;
            }
        }

        if (allUnchecked) {
            allPokemonChip.setChecked(true);
            listaPokemonAdapter.genAllList("Gen");
            aptoParaCargar = true;
        }
    }

    private void filterChipFalse() {
        Chip[] allChips = {allPokemonChip, gen1PokemonChip, gen2PokemonChip, gen3PokemonChip, gen4PokemonChip, gen5PokemonChip, gen6PokemonChip, gen7PokemonChip, gen8PokemonChip, gen9PokemonChip};
        for (Chip chip : allChips) {
            if (chip == allPokemonChip) {
                chip.setChecked(true);
            } else {
                chip.setChecked(false);
            }
        }
    }

    private String calcularGeneracion(int pokemonNumber) {
        if (pokemonNumber <= 151) {
            return "Gen1";
        } else if (pokemonNumber <= 251) {
            return "Gen2";
        } else if (pokemonNumber <= 386) {
            return "Gen3";
        } else if (pokemonNumber <= 493) {
            return "Gen4";
        } else if (pokemonNumber <= 649) {
            return "Gen5";
        } else if (pokemonNumber <= 721) {
            return "Gen6";
        } else if (pokemonNumber <= 809) {
            return "Gen7";
        } else if (pokemonNumber <= 905) {
            return "Gen8";
        } else {
            return "Gen9";
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterChipFalse();
        listaPokemonAdapter.filter(newText);
        return false;
    }
}