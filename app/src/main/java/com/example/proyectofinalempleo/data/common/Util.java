package com.example.proyectofinalempleo.data.common;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import retrofit2.Response;

public class Util {
    private static final Gson gson = new Gson();
    private static final String TAG = "RetrofitUtils";

    /**
     * Procesa errores de Retrofit de forma genérica.
     * @param response La respuesta de error del servidor.
     * @return Un objeto BaseResponse con el mensaje de error traducido.
     */
    public static <T> BaseResponse<T> getBaseResponseError(Response<BaseResponse<T>> response) {
        try {
            // 1. Verificar si hay cuerpo de error
            if (response.errorBody() == null) {
                return BaseResponse.error("Error sin mensaje del servidor (Código: " + response.code() + ")");
            }

            String errorBodyString = response.errorBody().string();

            // 2. Intentar convertir el JSON de error al molde BaseResponse
            try {
                BaseResponse<T> errorResponse = gson.fromJson(errorBodyString,
                        new TypeToken<BaseResponse<T>>() {}.getType());
                if (errorResponse != null && errorResponse.getMessage() != null) {
                    return errorResponse;
                }
            } catch (Exception e) {
                // Si no es un JSON válido, continuamos al análisis de texto
            }

            // 3. Manejar respuestas HTML (errores 500 del servidor)
            if (errorBodyString.contains("<!DOCTYPE html>")) {
                return BaseResponse.error("Error interno del servidor (HTTP " + response.code() + ").");
            }

            // 4. Retornar el texto plano si existe, o un error genérico
            return BaseResponse.error(!errorBodyString.isEmpty() ? errorBodyString : "Error desconocido");

        } catch (Exception e) {
            Log.e(TAG, "Excepción crítica al procesar error: " + e.getMessage());
            return BaseResponse.error("Error de comunicación: Fallo al leer la respuesta.");
        }
    }
}