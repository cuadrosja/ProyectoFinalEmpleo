package com.example.proyectofinalempleo.presentacion.favorito;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Favorito;
import com.example.proyectofinalempleo.data.repository.FavoritoRepository;

import java.util.List;

public class FavoritoViewModel extends ViewModel {

    private final FavoritoRepository repository;

    public FavoritoViewModel() {
        this.repository = new FavoritoRepository();
    }

    // Cambiado de 'getFavoritos' a 'listarFavoritos' para que coincida con tu Repository
    public LiveData<BaseResponse<List<Favorito>>> listarFavoritos(int idUsuario) {
        return repository.listarFavoritos(idUsuario);
    }

    // Agregamos este para que puedas registrar favoritos desde el detalle
    public LiveData<BaseResponse<Favorito>> registrarFavorito(com.example.proyectofinalempleo.data.request.FavoritoRequest request) {
        return repository.registrarFavorito(request);
    }
    public LiveData<BaseResponse<Void>> eliminarFavorito(int idFavorito) {
        return repository.eliminarFavorito(idFavorito);
    }
}