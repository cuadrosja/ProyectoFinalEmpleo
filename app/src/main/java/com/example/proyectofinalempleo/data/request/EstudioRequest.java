package com.example.proyectofinalempleo.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioRequest {
    private int idUsuario;
    private String institucion;
    private String tituloObtenido;
    private String fechaInicio;
    private String fechaFin;
    private String estado;
}
