package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Postulacion;
import com.example.proyectofinalempleo.data.request.PostulacionRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostulacionApi {
    @POST("postulaciones")
    Call<BaseResponse<Postulacion>> registrarPostulacion(@Body PostulacionRequest request);
    @GET("postulaciones/usuario/{id}")
    Call<BaseResponse<List<Postulacion>>> obtenerMisPostulaciones(@Path("id") int idUsuario);
    @DELETE("postulaciones/{id}")
    Call<BaseResponse<Void>> cancelarPostulacion(@Path("id") int idPostulacion);
}
