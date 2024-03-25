package projectofinal.alternativedex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.models.Pokemon;

public class ListaPokemonAdapter extends RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder> {

    private ArrayList<Pokemon> dataset;
    private List<Pokemon> originalPokemon;
    private Context context;

    public ListaPokemonAdapter(Context context){
        this.context = context;
        dataset = new ArrayList<>();
        originalPokemon = new ArrayList<>(dataset);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imagen_pokemon, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Pokemon pokemon = dataset.get(position);
                Intent intent = pokemon.getIntent(v.getContext());
                v.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon p = dataset.get(position);
        holder.nombreTextView.setText(p.getName());

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                        + p.getNumberPNG() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fotoImagenView);

        String n;

        if (p.getNumberPNG() < 10) {
            n = "N.º 000" + String.valueOf(p.getNumberPNG());
        } else if (p.getNumberPNG() < 100) {
            n = "N.º 00" + String.valueOf(p.getNumberPNG());
        } else if (p.getNumberPNG() < 1000) {
            n = "N.º 0" + String.valueOf(p.getNumberPNG());
        } else {
            n = "N.º" + String.valueOf(p.getNumberPNG());
        }

        holder.numeroPokemon.setText(n);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void filter(final String strSearch){
        if (strSearch.length() == 0){
            dataset.clear();
            dataset.addAll(originalPokemon);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Pokemon> collect = originalPokemon.stream()
                        .filter(d -> d.getName().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());
                dataset.clear();
                dataset.addAll(collect);
            } else {
                dataset.clear();
                for(Pokemon p : originalPokemon){
                    if (p.getName().toLowerCase().contains(strSearch)){
                        dataset.add(p);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


    public void adicionarListaPokemon(ArrayList<Pokemon> listaPokemon){
        dataset.addAll(listaPokemon);
        notifyDataSetChanged();
    }

    public void actualizarOriginalPokemon() {
        originalPokemon.clear();
        originalPokemon.addAll(dataset);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView numeroPokemon;
        private ImageView fotoImagenView;

        private TextView nombreTextView;

        public ViewHolder(View itemView){
            super(itemView);
            numeroPokemon = itemView.findViewById(R.id.numeroPokemons);
            fotoImagenView = itemView.findViewById(R.id.fotoPokemon);
            nombreTextView = itemView.findViewById(R.id.nombrePokemon);
        }

    }
}
