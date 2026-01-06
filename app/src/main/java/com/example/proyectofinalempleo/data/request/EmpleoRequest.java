package com.example.proyectofinalempleo.data.request;

import lombok.Data;

@Data
public class EmpleoRequest {
    private int idEmpresa;
    private int idCategoria;
    private String tituloEmpleo;
    private String puesto;
    private String descripcion;
    private String modalidad;
}
