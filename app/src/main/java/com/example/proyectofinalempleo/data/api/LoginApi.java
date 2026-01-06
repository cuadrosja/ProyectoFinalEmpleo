package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Usuario;
import com.example.proyectofinalempleo.data.request.LoginUsuarioRequest;
import com.example.proyectofinalempleo.data.request.RegistroRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//Esta interfaz usa la librería Retrofit para enviar y recibir datos al servidor para los procesos de Login y Registro
public interface LoginApi {
    //Iniciar sesión
    @POST("usuarios/login")
    Call<BaseResponse<Usuario>> inicioSesion(@Body LoginUsuarioRequest request);
}
