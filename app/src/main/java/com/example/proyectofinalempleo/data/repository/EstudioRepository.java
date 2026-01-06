package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.EstudioApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.Estudio;
import com.example.proyectofinalempleo.data.request.EstudioRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstudioRepository {
    private final EstudioApi api;
    private final String TAG = "EstudioRepository";

    public EstudioRepository() {
        this.api = RetrofitClient.getEstudioApiService();
    }
    public LiveData<BaseResponse<List<Estudio>>> listarEstudios(int idUsuario) {
        MutableLiveData<BaseResponse<List<Estudio>>> data = new MutableLiveData<>();
        api.obtenerEstudiosPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<Estudio>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Estudio>>> call, Response<BaseResponse<List<Estudio>>> response) {
                if (response.isSuccessful() && response.body() != null) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<List<Estudio>>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de conexión al obtener estudios."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<Estudio>> registrarEstudio(EstudioRequest request) {
        MutableLiveData<BaseResponse<Estudio>> data = new MutableLiveData<>();
        api.registrarEstudio(request).enqueue(new Callback<BaseResponse<Estudio>>() {
            @Override
            public void onResponse(Call<BaseResponse<Estudio>> call, Response<BaseResponse<Estudio>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Estudio>> call, Throwable t) {
                data.setValue(BaseResponse.error("Error de red al registrar."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<Estudio>> actualizarEstudio(int id, EstudioRequest request) {
        MutableLiveData<BaseResponse<Estudio>> data = new MutableLiveData<>();
        api.actualizarEstudio(id, request).enqueue(new Callback<BaseResponse<Estudio>>() {
            @Override
            public void onResponse(Call<BaseResponse<Estudio>> call, Response<BaseResponse<Estudio>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Estudio>> call, Throwable t) {
                data.setValue(BaseResponse.error("Error de red al actualizar."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<Void>> eliminarEstudio(int id) {
        MutableLiveData<BaseResponse<Void>> data = new MutableLiveData<>();
        api.eliminarEstudio(id).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                data.setValue(BaseResponse.error("Error al eliminar el estudio."));
            }
        });
        return data;
    }
}