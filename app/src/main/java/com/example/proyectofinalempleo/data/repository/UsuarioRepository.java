package com.example.proyectofinalempleo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.proyectofinalempleo.data.api.LoginApi;
import com.example.proyectofinalempleo.data.api.RegistrarApi;
import com.example.proyectofinalempleo.data.api.RetrofitClient;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.common.Util;
import com.example.proyectofinalempleo.data.model.Usuario;
import com.example.proyectofinalempleo.data.request.LoginUsuarioRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.proyectofinalempleo.data.request.RegistroRequest;

public class UsuarioRepository {

    private final LoginApi loginApi;
    private final RegistrarApi registrarApi;
    private final String TAG = UsuarioRepository.class.getSimpleName();

    public UsuarioRepository() {
        // Inicializa Retrofit y crea la instancia de tu API
        this.loginApi = RetrofitClient.getLoginApiService();
        this.registrarApi = RetrofitClient.getRegistrarApiService();
    }


    public LiveData<BaseResponse<Usuario>> obtenerUsuarioPorIdUsuario(int idUsuario) {
        Log.i(TAG, "==> Iniciando petición obtenerUsuario para ID: " + idUsuario);
        MutableLiveData<BaseResponse<Usuario>> data = new MutableLiveData<>();

        registrarApi.obtenerUsuarioPorIdUsuario(idUsuario).enqueue(new Callback<BaseResponse<Usuario>>() {
            @Override
            public void onResponse(Call<BaseResponse<Usuario>> call, Response<BaseResponse<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario u = response.body().getData();

                    // --- VERIFICACIÓN CLAVE ---
                    if (u != null) {
                        Log.d(TAG, "EXITO: Usuario encontrado");
                        Log.d(TAG, "CARTA RECIBIDA: [" + u.getCartaPresentacion() + "]");
                    } else {
                        Log.w(TAG, "ERROR: La respuesta llegó pero el objeto 'data' está nulo");
                    }
                    // --------------------------

                    data.setValue(response.body());
                } else {
                    Log.e(TAG, "ERROR EN RESPUESTA: Código " + response.code());
                    data.setValue(Util.getBaseResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Usuario>> call, Throwable t) {
                Log.e(TAG, "FALLO CRÍTICO DE CONEXIÓN: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de conexión al obtener el perfil."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<Usuario>> inicioSesion(LoginUsuarioRequest request) {
        Log.i(TAG, "Iniciando peticion inicioSesion");
        MutableLiveData<BaseResponse<Usuario>> data = new MutableLiveData<>();

        loginApi.inicioSesion(request).enqueue(new Callback<BaseResponse<Usuario>>() {
            @Override
            public void onResponse(Call<BaseResponse<Usuario>> call, Response<BaseResponse<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Usuario>> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión. Revise la IP y el Firewall."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<Usuario>> registrarUsuario(RegistroRequest request) {
        Log.i(TAG, "Iniciando peticion registro");
        MutableLiveData<BaseResponse<Usuario>> data = new MutableLiveData<>();

        registrarApi.registrarUsuario(request).enqueue(new Callback<BaseResponse<Usuario>>() {
            @Override
            public void onResponse(Call<BaseResponse<Usuario>> call, Response<BaseResponse<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    // USAS LA CLASE UTIL AQUÍ TAMBIÉN
                    data.setValue(Util.getBaseResponseError(response));
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Usuario>> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                data.setValue(BaseResponse.error("Fallo de conexión al registrar."));
            }
        });
        return data;
    }
    public LiveData<BaseResponse<Usuario>> actualizarUsuario(int id, RegistroRequest request) {
        MutableLiveData<BaseResponse<Usuario>> data = new MutableLiveData<>();
        registrarApi.actualizarUsuario(id, request).enqueue(new Callback<BaseResponse<Usuario>>() {
            @Override
            public void onResponse(Call<BaseResponse<Usuario>> call, Response<BaseResponse<Usuario>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Util.getBaseResponseError(response));
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Usuario>> call, Throwable t) {
                Log.e(TAG, "Error en Actualizar Usuario: " + t.getMessage());
                data.setValue(BaseResponse.error("Error de red al actualizar el perfil."));
            }
        });
        return data;
    }
}