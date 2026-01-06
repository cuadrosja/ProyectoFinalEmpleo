package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Usuario;
import com.example.proyectofinalempleo.data.request.RegistroRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RegistrarApi {

    @GET("usuarios/{id}")
    Call<BaseResponse<Usuario>> obtenerUsuarioPorIdUsuario(@Path("id") int idUsuario);
    @POST("usuarios/registrar")
    Call<BaseResponse<Usuario>> registrarUsuario(@Body RegistroRequest request);
    @PUT("usuarios/{id}")
    Call<BaseResponse<Usuario>> actualizarUsuario(@Path("id") int id, @Body RegistroRequest request);
}

