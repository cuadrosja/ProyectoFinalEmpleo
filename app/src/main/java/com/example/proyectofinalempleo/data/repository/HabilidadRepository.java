package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.HabilidadApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.Habilidad;
import com.example.proyectofinalempleo.data.request.HabilidadRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabilidadRepository {
    private final HabilidadApi api;
    private final String TAG = "HabilidadRepository";

    public HabilidadRepository() {
        this.api = RetrofitClient.getHabilidadApiService();
    }

    public LiveData<BaseResponse<List<Habilidad>>> listarHabilidades(int idUsuario) {
        MutableLiveData<BaseResponse<List<Habilidad>>> data = new MutableLiveData<>();
        api.obtenerHabilidadesPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<Habilidad>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Habilidad>>> call, Response<BaseResponse<List<Habilidad>>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<List<Habilidad>>> call, Throwable t) {
                Log.e(TAG, "Error en Listar Habilidades: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de conexión."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<Habilidad>> registrarHabilidad(HabilidadRequest request) {
        MutableLiveData<BaseResponse<Habilidad>> data = new MutableLiveData<>();
        api.registrarHabilidad(request).enqueue(new Callback<BaseResponse<Habilidad>>() {
            @Override
            public void onResponse(Call<BaseResponse<Habilidad>> call, Response<BaseResponse<Habilidad>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Habilidad>> call, Throwable t) {
                Log.e(TAG, "Error en Registrar Habilidad: " + t.getMessage());
                data.setValue(BaseResponse.error("Error al guardar habilidad."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<Habilidad>> actualizarHabilidad(int id, HabilidadRequest request) {
        MutableLiveData<BaseResponse<Habilidad>> data = new MutableLiveData<>();

        api.actualizarHabilidad(id, request).enqueue(new Callback<BaseResponse<Habilidad>>() {
            @Override
            public void onResponse(Call<BaseResponse<Habilidad>> call, Response<BaseResponse<Habilidad>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Habilidad>> call, Throwable t) {
                Log.e(TAG, "Error en Actualizar Habilidad: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al actualizar la habilidad."));
            }
        });

        return data;
    }
    public LiveData<BaseResponse<Void>> eliminarHabilidad(int id) {
        MutableLiveData<BaseResponse<Void>> data = new MutableLiveData<>();
        api.eliminarHabilidad(id).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Error en Eliminar Habilidad: " + t.getMessage());
                data.setValue(BaseResponse.error("Error al eliminar habilidad."));
            }
        });
        return data;
    }
}