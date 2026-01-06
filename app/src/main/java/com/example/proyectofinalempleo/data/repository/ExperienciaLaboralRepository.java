package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.ExperienciaLaboralApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.ExperienciaLaboral;
import com.example.proyectofinalempleo.data.request.ExperienciaLaboralRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienciaLaboralRepository {

    private final ExperienciaLaboralApi api;
    private final String TAG = ExperienciaLaboralRepository.class.getSimpleName();

    public ExperienciaLaboralRepository() {
        this.api = RetrofitClient.getExperienciaLaboralApiService();
    }

    public LiveData<BaseResponse<ExperienciaLaboral>> registrarExperiencia(ExperienciaLaboralRequest request) {
        MutableLiveData<BaseResponse<ExperienciaLaboral>> data = new MutableLiveData<>();
        api.registrarExperiencia(request).enqueue(new Callback<BaseResponse<ExperienciaLaboral>>() {
            @Override
            public void onResponse(Call<BaseResponse<ExperienciaLaboral>> call, Response<BaseResponse<ExperienciaLaboral>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    // Usamos la herramienta común
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ExperienciaLaboral>> call, Throwable t) {
                Log.e(TAG, "Fallo registro: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al registrar experiencia."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<List<ExperienciaLaboral>>> listarExperiencia(int idUsuario) {
        MutableLiveData<BaseResponse<List<ExperienciaLaboral>>> data = new MutableLiveData<>();
        api.obtenerExperienciasPorUsuario(idUsuario).enqueue(new Callback<BaseResponse<List<ExperienciaLaboral>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ExperienciaLaboral>>> call, Response<BaseResponse<List<ExperienciaLaboral>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    // Usamos la herramienta común
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ExperienciaLaboral>>> call, Throwable t) {
                Log.e(TAG, "Fallo listar: " + t.getMessage());
                data.setValue(BaseResponse.error("Fallo de conexión. Verifique su red."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<ExperienciaLaboral>> actualizarExperiencia(int id, ExperienciaLaboralRequest request) {
        MutableLiveData<BaseResponse<ExperienciaLaboral>> data = new MutableLiveData<>();
        api.actualizarExperiencia(id, request).enqueue(new Callback<BaseResponse<ExperienciaLaboral>>() {
            @Override
            public void onResponse(Call<BaseResponse<ExperienciaLaboral>> call, Response<BaseResponse<ExperienciaLaboral>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ExperienciaLaboral>> call, Throwable t) {
                Log.e(TAG, "Fallo actualización: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al actualizar registro."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<Void>> eliminarExperiencia(int id) {
        MutableLiveData<BaseResponse<Void>> data = new MutableLiveData<>();
        api.eliminarExperiencia(id).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Fallo eliminación: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al intentar eliminar."));
            }
        });
        return data;
    }
}