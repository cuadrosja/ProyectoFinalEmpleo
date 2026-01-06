package com.example.proyectofinalempleo.data.common;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor

//Esta clase garantiza que, sin importar lo que pida el cliente, la respuesta siempre tendrá la misma estructura
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(true, "Ok", data);
    }
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<T>(false, message, null);
    }
    // NUEVO MÉTODO (2 argumentos) para que no marque error en el Repo
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<T>(true, message, data);
    }
}




