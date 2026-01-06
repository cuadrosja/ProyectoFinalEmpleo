package com.example.proyectofinalempleo.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaLaboral {
    private int idExperiencia;
    private int idUsuario;
    private String nombreEmpresa;
    private String puestoOcupado;
    private String descripcionTareas;
    private String fechaInicio;
    private String fechaFin;
    private boolean trabajoActual;
}
