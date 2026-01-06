package com.example.proyectofinalempleo.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habilidad {
    private int idHabilidad;
    private int idUsuario;
    private String nombreHabilidad;
}
