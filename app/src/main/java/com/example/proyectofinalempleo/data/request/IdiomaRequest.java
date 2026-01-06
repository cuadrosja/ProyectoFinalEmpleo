package com.example.proyectofinalempleo.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdiomaRequest {
    private int idUsuario;
    private String nombreIdioma;
    private String nivel;
}
