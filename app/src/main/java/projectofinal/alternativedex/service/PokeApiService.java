package projectofinal.alternativedex.service;

import projectofinal.alternativedex.models.PokemonDetalle;
import projectofinal.alternativedex.models.PokemonRespuesta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeApiService {

    @GET("pokemon")
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{pokemonId}")
    Call<PokemonDetalle> obtenerDetallePokemon(@Path("pokemonId") int pokemonId);
}
