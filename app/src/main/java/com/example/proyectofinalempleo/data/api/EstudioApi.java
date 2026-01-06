package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Estudio;
import com.example.proyectofinalempleo.data.request.EstudioRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface EstudioApi {
    @GET("estudios/usuario/{idUsuario}")
    Call<BaseResponse<List<Estudio>>> obtenerEstudiosPorUsuario(@Path("idUsuario") int idUsuario);
    @POST("estudios")
    Call<BaseResponse<Estudio>> registrarEstudio(@Body EstudioRequest request);
    @PUT("estudios/{id}")
    Call<BaseResponse<Estudio>> actualizarEstudio(@Path("id") int id, @Body EstudioRequest request);
    @DELETE("estudios/{id}")
    Call<BaseResponse<Void>> eliminarEstudio(@Path("id") int id);
}
