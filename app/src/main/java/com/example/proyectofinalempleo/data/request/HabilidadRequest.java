package com.example.proyectofinalempleo.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabilidadRequest {
    private int idUsuario;
    private String nombreHabilidad;
}
