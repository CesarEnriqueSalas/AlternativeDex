package projectofinal.alternativedex.models;

import java.util.List;

public class PokemonDetalle {
    private String name;
    private int weight;
    private List<Type> types;
    private List<Stat> stats;

    public PokemonDetalle(String name, int weight, List<Type> types, List<Stat> stats) {
        this.name = name;
        this.weight = weight;
        setTypes(types); // Llama al setter para inicializar y convertir los tipos
        this.stats = stats;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
        convertirTiposAEspanol(); // Llama al método para convertir los tipos a español
    }

    public List<Stat> getStats() {
        return stats;
    }

    public static class Type {
        private TypeDetail type;

        public TypeDetail getType() {
            return type;
        }

        public void setType(TypeDetail type) {
            this.type = type;
        }
    }

    public static class Stat {
        private StatDetail stat;
        private int base_stat;

        public StatDetail getStat() {
            return stat;
        }

        public void setStat(StatDetail stat) {
            this.stat = stat;
        }

        public int getBaseStat() {
            return base_stat;
        }

        public void setBaseStat(int base_stat) {
            this.base_stat = base_stat;
        }
    }

    public static class TypeDetail {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class StatDetail {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getTipoEnEspanol(String typeName) {
        switch (typeName.toLowerCase()) {
            case "normal":
                return "NORMAL";
            case "fire":
                return "FUEGO";
            case "water":
                return "AGUA";
            case "electric":
                return "ELÉCTRICO";
            case "grass":
                return "PLANTA";
            case "ice":
                return "HIELO";
            case "fighting":
                return "LUCHA";
            case "poison":
                return "VENENO";
            case "ground":
                return "TIERRA";
            case "flying":
                return "VOLADOR";
            case "psychic":
                return "PSÍQUICO";
            case "bug":
                return "BICHO";
            case "rock":
                return "ROCA";
            case "ghost":
                return "FANTASMA";
            case "dragon":
                return "DRAGÓN";
            case "dark":
                return "SINIESTRO";
            case "steel":
                return "ACERO";
            case "fairy":
                return "HADA";
            default:
                return "DESCONOCIDO";
        }
    }

    public void convertirTiposAEspanol() {
        if (types != null) {
            for (Type type : types) {
                String typeName = type.getType().getName();
                String typeNameInSpanish = getTipoEnEspanol(typeName);
                type.getType().setName(typeNameInSpanish);
            }
        }
    }
}
