package com.example.proyectofinalempleo.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorito {
    private int idFavorito;
    private int idUsuario;
    private int idEmpleo;
    private String fechaGuardado;

    // Relación para mostrar datos del empleo en la lista de favoritos
    private Empleo empleo;
}