package com.example.proyectofinalempleo.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoRequest {
    private int idUsuario;
    private int idEmpleo;
}
