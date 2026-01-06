package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.ExperienciaLaboral;
import com.example.proyectofinalempleo.data.request.ExperienciaLaboralRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExperienciaLaboralApi {

    // Obtener historial del usuario
    @GET("experiencias/usuario/{idUsuario}")
    Call<BaseResponse<List<ExperienciaLaboral>>> obtenerExperienciasPorUsuario(@Path("idUsuario") int idUsuario);
    // Guardar nueva experiencia
    @POST("experiencias")
    Call<BaseResponse<ExperienciaLaboral>> registrarExperiencia(@Body ExperienciaLaboralRequest request);
    // Actualizar experiencia existente (Editar)
    @PUT("experiencias/{id}")
    Call<BaseResponse<ExperienciaLaboral>> actualizarExperiencia(@Path("id") int id, @Body ExperienciaLaboralRequest request);
    // Eliminar experiencia
    @DELETE("experiencias/{id}")
    Call<BaseResponse<Void>> eliminarExperiencia(@Path("id") int id);
}
