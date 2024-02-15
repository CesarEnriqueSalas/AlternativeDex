package projectofinal.alternativedex.models;

import java.util.List;

public class PokemonDetalle {
    private List<Stat> stats;
    private List<Tipo> types;
    private int weight;

    public PokemonDetalle(List<Stat> stats, List<Tipo> types, int weight) {
        this.stats = stats;
        this.types = types;
        this.weight = weight;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    public List<Tipo> getTypes() {
        return types;
    }

    public void setTypes(List<Tipo> types) {
        this.types = types;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
