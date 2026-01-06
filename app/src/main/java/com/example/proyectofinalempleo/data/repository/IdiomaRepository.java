package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.IdiomaApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.Idioma;
import com.example.proyectofinalempleo.data.request.IdiomaRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdiomaRepository {
    private final IdiomaApi api;
    private final String TAG = "IdiomaRepository";

    public IdiomaRepository() {
        this.api = RetrofitClient.getIdiomaApiService();
    }

    public LiveData<BaseResponse<List<Idioma>>> listarIdiomas(int idUsuario) {
        MutableLiveData<BaseResponse<List<Idioma>>> data = new MutableLiveData<>();
        api.obtenerIdiomasPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<Idioma>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Idioma>>> call, Response<BaseResponse<List<Idioma>>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<List<Idioma>>> call, Throwable t) {
                Log.e(TAG, "Error listar: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de conexión al obtener idiomas."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<Idioma>> registrarIdioma(IdiomaRequest request) {
        MutableLiveData<BaseResponse<Idioma>> data = new MutableLiveData<>();
        api.registrarIdioma(request).enqueue(new Callback<BaseResponse<Idioma>>() {
            @Override
            public void onResponse(Call<BaseResponse<Idioma>> call, Response<BaseResponse<Idioma>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Idioma>> call, Throwable t) {
                Log.e(TAG, "Error registro: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al registrar idioma."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<Idioma>> actualizarIdioma(int id, IdiomaRequest request) {
        MutableLiveData<BaseResponse<Idioma>> data = new MutableLiveData<>();
        api.actualizarIdioma(id, request).enqueue(new Callback<BaseResponse<Idioma>>() {
            @Override
            public void onResponse(Call<BaseResponse<Idioma>> call, Response<BaseResponse<Idioma>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Idioma>> call, Throwable t) {
                Log.e(TAG, "Error actualizar: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al actualizar idioma."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<Void>> eliminarIdioma(int id) {
        MutableLiveData<BaseResponse<Void>> data = new MutableLiveData<>();
        api.eliminarIdioma(id).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else data.setValue(Util.getBaseResponseError(response));
            }
            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Error eliminar: " + t.getMessage());
                data.setValue(BaseResponse.error("Error al eliminar el idioma."));
            }
        });
        return data;
    }
}