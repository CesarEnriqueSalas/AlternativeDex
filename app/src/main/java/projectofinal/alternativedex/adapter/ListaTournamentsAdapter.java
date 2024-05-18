package projectofinal.alternativedex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.models.Tournaments;

public class ListaTournamentsAdapter extends RecyclerView.Adapter<ListaTournamentsAdapter.ViewHolder> {

    Context context;
    ArrayList<Tournaments> tournamentsArrayList;

    public ListaTournamentsAdapter(Context context, ArrayList<Tournaments> tournamentsArrayList) {
        this.context = context;
        this.tournamentsArrayList = tournamentsArrayList;
    }

    @NonNull
    @Override
    public ListaTournamentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(context).inflate(R.layout.imagen_tournaments, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaTournamentsAdapter.ViewHolder holder, int position) {
        Tournaments tournaments = tournamentsArrayList.get(position);

        holder.name.setText(tournaments.getNombre());
        holder.organizador.setText(tournaments.getOrganizador());
        holder.lugar.setText(tournaments.getLugarEvento());
        holder.fecha.setText(tournaments.getFecha());

    }

    @Override
    public int getItemCount() {
        return tournamentsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, organizador, lugar, fecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textNameRegional);
            organizador = itemView.findViewById(R.id.textOrganizadorRegional);
            lugar = itemView.findViewById(R.id.textLugarRegional);
            fecha = itemView.findViewById(R.id.textFechaRegional);
        }
    }
}
