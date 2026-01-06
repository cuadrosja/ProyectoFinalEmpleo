package com.example.proyectofinalempleo.data.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequest {
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String nombreUsuario;
    private String contrasena;
    private String cartaPresentacion;
    private String fotoPerfil;
}
