package com.example.proyectofinalempleo.data.api;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Empleo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmpleoApi {
    @GET("empleos")
    Call<BaseResponse<List<Empleo>>> obtenerTodosLosEmpleos(
            @Query("limit") int limite,
            @Query("page") int pagina,
            @Query("search") String busqueda,
            @Query("location") String ubicacion);
}
