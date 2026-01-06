package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Favorito;
import com.example.proyectofinalempleo.data.request.FavoritoRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoritoApi {
    @POST("favoritos")
    Call<BaseResponse<Favorito>> registrarFavorito(@Body FavoritoRequest request);
    @GET("favoritos/usuario/{idUsuario}")
    Call<BaseResponse<List<Favorito>>> obtenerMisFavoritos(@Path("idUsuario") int idUsuario);
    @DELETE("favoritos/{id}")
    Call<BaseResponse<Void>> eliminarFavorito(@Path("id") int idFavorito);
}
