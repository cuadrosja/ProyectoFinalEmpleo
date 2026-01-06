package com.example.proyectofinalempleo.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Establecer la comunicación entre la app Android y el servidor backend API, asegurando
// que solo haya una conexión eficiente a la vez.
public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.18.5:3000/api/v1/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static LoginApi getLoginApiService() {
        return getRetrofit().create(LoginApi.class);
    }
    public static RegistrarApi getRegistrarApiService() {
        return getRetrofit().create(RegistrarApi.class);
    }
    public static EmpleoApi getEmpleoApiService() {
        return getRetrofit().create(EmpleoApi.class);
    }
    public static PostulacionApi getPostulacionesApiService() {
        return getRetrofit().create(PostulacionApi.class);
    }
    public static ExperienciaLaboralApi getExperienciaLaboralApiService() {
        return getRetrofit().create(ExperienciaLaboralApi.class);
    }
    public static FavoritoApi getFavoritoApiService() {
        return getRetrofit().create(FavoritoApi.class);
    }
    public static IdiomaApi getIdiomaApiService() {
        return getRetrofit().create(IdiomaApi.class);
    }

    public static EstudioApi getEstudioApiService() {
        return getRetrofit().create(EstudioApi.class);
    }

    public static HabilidadApi getHabilidadApiService() {
        return getRetrofit().create(HabilidadApi.class);
    }
}
