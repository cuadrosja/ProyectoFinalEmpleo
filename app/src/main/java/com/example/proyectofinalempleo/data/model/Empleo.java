package com.example.proyectofinalempleo.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleo {
    private int idEmpleo;
    private int idEmpresa;
    private int idCategoria;
    private String tituloEmpleo;
    private String puesto;
    private String descripcion;
    private String modalidad;
    private boolean estaActivo;
    private String fechaPublicacion;

    private Empresa empresa;
    private Categoria categoria;
}