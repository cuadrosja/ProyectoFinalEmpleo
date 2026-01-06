package com.example.proyectofinalempleo.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaLaboralRequest {
    private String nombreEmpresa;
    private String puestoOcupado;
    private String descripcionTareas;
    private String fechaInicio;
    private String fechaFin;
    private boolean trabajoActual;
    private int idUsuario;
}
