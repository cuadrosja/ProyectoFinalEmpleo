package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.EmpleoApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.Empleo;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleoRepository {

    private final EmpleoApi empleoApi;
    private final String TAG = EmpleoRepository.class.getSimpleName();

    public EmpleoRepository() {
        this.empleoApi = RetrofitClient.getEmpleoApiService();
    }

    public LiveData<BaseResponse<List<Empleo>>> listarEmpleos(int limite, int pagina, String busqueda, String ubicacion) {
        MutableLiveData<BaseResponse<List<Empleo>>> data = new MutableLiveData<>();

        empleoApi.obtenerTodosLosEmpleos(limite, pagina, busqueda, ubicacion).enqueue(new Callback<BaseResponse<List<Empleo>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Empleo>>> call, Response<BaseResponse<List<Empleo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<List<Empleo>>> call, Throwable t) {
                Log.e(TAG, "Error red: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de conexión al servidor."));
            }
        });
        return data;
    }
}