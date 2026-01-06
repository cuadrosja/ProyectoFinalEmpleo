package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Habilidad;
import com.example.proyectofinalempleo.data.request.HabilidadRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HabilidadApi {
    @GET("habilidades/usuario/{idUsuario}")
    Call<BaseResponse<List<Habilidad>>> obtenerHabilidadesPorUsuario(@Path("idUsuario") int idUsuario);
    @POST("habilidades")
    Call<BaseResponse<Habilidad>> registrarHabilidad(@Body HabilidadRequest request);
    @PUT("habilidades/{id}")
    Call<BaseResponse<Habilidad>> actualizarHabilidad(@Path("id") int id, @Body HabilidadRequest request);
    @DELETE("habilidades/{id}")
    Call<BaseResponse<Void>> eliminarHabilidad(@Path("id") int id);
}
