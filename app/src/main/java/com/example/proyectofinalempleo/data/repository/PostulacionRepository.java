package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.PostulacionApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.Postulacion;
import com.example.proyectofinalempleo.data.request.PostulacionRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostulacionRepository {

    private final PostulacionApi postulacionApi;
    private final String TAG = PostulacionRepository.class.getSimpleName();

    public PostulacionRepository() {
        this.postulacionApi = RetrofitClient.getPostulacionesApiService();
    }

    public LiveData<BaseResponse<Postulacion>> registrarPostulacion(PostulacionRequest request) {
        MutableLiveData<BaseResponse<Postulacion>> data = new MutableLiveData<>();

        postulacionApi.registrarPostulacion(request).enqueue(new Callback<BaseResponse<Postulacion>>() {
            @Override
            public void onResponse(Call<BaseResponse<Postulacion>> call, Response<BaseResponse<Postulacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Postulacion>> call, Throwable t) {
                data.setValue(BaseResponse.error("Fallo de red al intentar postular."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<List<Postulacion>>> obtenerMisPostulaciones(int idUsuario) {
        MutableLiveData<BaseResponse<List<Postulacion>>> data = new MutableLiveData<>();

        postulacionApi.obtenerMisPostulaciones(idUsuario).enqueue(new Callback<BaseResponse<List<Postulacion>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Postulacion>>> call, Response<BaseResponse<List<Postulacion>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    // CAMBIO: Ahora usamos Util para capturar el error real del backend
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Postulacion>>> call, Throwable t) {
                Log.e(TAG, "Error de red: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de conexión con el servidor."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<Void>> cancelarPostulacion(int idPostulacion) {
        MutableLiveData<BaseResponse<Void>> data = new MutableLiveData<>();

        postulacionApi.cancelarPostulacion(idPostulacion).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    data.setValue(BaseResponse.success(null, "Postulación cancelada correctamente."));
                } else {
                    // CAMBIO: Usamos Util para manejar errores 404 o 500
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Fallo de conexión: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al intentar cancelar."));
            }
        });
        return data;
    }
}