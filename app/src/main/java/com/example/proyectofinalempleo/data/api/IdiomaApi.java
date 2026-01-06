package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Idioma;
import com.example.proyectofinalempleo.data.request.IdiomaRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IdiomaApi {
    @GET("idiomas/usuario/{idUsuario}")
    Call<BaseResponse<List<Idioma>>> obtenerIdiomasPorUsuario(@Path("idUsuario") int idUsuario);
    @POST("idiomas")
    Call<BaseResponse<Idioma>> registrarIdioma(@Body IdiomaRequest request);
    @PUT("idiomas/{id}")
    Call<BaseResponse<Idioma>> actualizarIdioma(@Path("id") int id, @Body IdiomaRequest request);
    @DELETE("idiomas/{id}")
    Call<BaseResponse<Void>> eliminarIdioma(@Path("id") int id);
}
