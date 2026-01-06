package com.example.proyectofinalempleo.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Idioma {
    private int idIdioma;
    private int idUsuario;
    private String nombreIdioma;
    private String nivel;
}
