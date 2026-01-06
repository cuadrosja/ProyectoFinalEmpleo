package com.example.proyectofinalempleo.data.request;

import lombok.Data;

@Data
public class LoginUsuarioRequest {
    private String nombreUsuario;
    private String contrasena;
}
