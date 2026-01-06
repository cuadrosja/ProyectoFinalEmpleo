package com.example.proyectofinalempleo.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postulacion {
    private int idPostulacion;
    private int idUsuario;
    private int idEmpleo;
    private String fechaPostulacion;
    private String estado;

    private Empleo empleo;
}
