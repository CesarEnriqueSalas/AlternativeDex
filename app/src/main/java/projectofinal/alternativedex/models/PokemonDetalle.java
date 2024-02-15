package projectofinal.alternativedex.models;

import java.util.List;

public class PokemonDetalle {
    private String name;
    private int weight;
    private List<Type> types;
    private List<Stat> stats;

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public static class Type {
        private TypeDetail type;

        public TypeDetail getType() {
            return type;
        }
    }

    public static class Stat {
        private StatDetail stat;
        private int base_stat;

        public StatDetail getStat() {
            return stat;
        }

        public int getBaseStat() {
            return base_stat;
        }
    }

    public static class TypeDetail {
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class StatDetail {
        private String name;

        public String getName() {
            return name;
        }
    }
}