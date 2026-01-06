package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.FavoritoApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.Favorito;
import com.example.proyectofinalempleo.data.request.FavoritoRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritoRepository {

    private final FavoritoApi api;
    private final String TAG = FavoritoRepository.class.getSimpleName();

    public FavoritoRepository() {
        // Obtenemos la conexión desde el cliente que configuramos antes
        this.api = RetrofitClient.getFavoritoApiService();
    }

    public LiveData<BaseResponse<Favorito>> registrarFavorito(FavoritoRequest request) {
        MutableLiveData<BaseResponse<Favorito>> data = new MutableLiveData<>();

        api.registrarFavorito(request).enqueue(new Callback<BaseResponse<Favorito>>() {
            @Override
            public void onResponse(Call<BaseResponse<Favorito>> call, Response<BaseResponse<Favorito>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    // Maneja errores como "Ya existe en favoritos" (400)
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Favorito>> call, Throwable t) {
                Log.e(TAG, "Error al guardar: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al guardar favorito."));
            }
        });
        return data;
    }

    public LiveData<BaseResponse<List<Favorito>>> listarFavoritos(int idUsuario) {
        MutableLiveData<BaseResponse<List<Favorito>>> data = new MutableLiveData<>();

        api.obtenerMisFavoritos(idUsuario).enqueue(new Callback<BaseResponse<List<Favorito>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Favorito>>> call, Response<BaseResponse<List<Favorito>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Favorito>>> call, Throwable t) {
                Log.e(TAG, "Error al listar: " + t.getMessage());
                data.setValue(BaseResponse.error("Fallo de conexión al cargar favoritos."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<Void>> eliminarFavorito(int idFavorito) {
        MutableLiveData<BaseResponse<Void>> data = new MutableLiveData<>();

        // Llamamos al método de la API para eliminar la relación usuario-empleo
        api.eliminarFavorito(idFavorito).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    // Si el servidor responde 200 OK o 204 No Content
                    data.setValue(response.body() != null ? response.body() : BaseResponse.success(null, "Eliminado con éxito"));
                } else {
                    // Maneja errores como "No encontrado" o problemas de servidor
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Error al eliminar: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al intentar eliminar favorito."));
            }
        });
        return data;
    }
}