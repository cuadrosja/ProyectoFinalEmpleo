package com.example.proyectofinalempleo.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

//Permite que la aplicación maneje los datos del usuario como un objeto simple y ordenado
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String nombreUsuario;
    private String contrasena;
    private String cartaPresentacion;
    private String fotoPerfil;
}