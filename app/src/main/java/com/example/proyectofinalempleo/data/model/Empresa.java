package com.example.proyectofinalempleo.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {
    private int idEmpresa;
    private String nombreEmpresa;
    private String email;
    private String descripcion;
    private String telefono;
    private String direccion;
}
