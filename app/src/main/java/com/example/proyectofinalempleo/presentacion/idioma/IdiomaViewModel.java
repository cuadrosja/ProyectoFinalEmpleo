package com.example.proyectofinalempleo.presentacion.idioma;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Idioma;
import com.example.proyectofinalempleo.data.request.IdiomaRequest;
import com.example.proyectofinalempleo.data.repository.IdiomaRepository;

import java.util.List;

public class IdiomaViewModel extends ViewModel {
    private final IdiomaRepository repository;

    public IdiomaViewModel() {
        this.repository = new IdiomaRepository();
    }

    public LiveData<BaseResponse<List<Idioma>>> obtenerIdiomas(int idUsuario) {
        return repository.listarIdiomas(idUsuario);
    }

    public LiveData<BaseResponse<Idioma>> registrarIdioma(IdiomaRequest request) {
        return repository.registrarIdioma(request);
    }

    public LiveData<BaseResponse<Idioma>> actualizarIdioma(int id, IdiomaRequest request) {
        return repository.actualizarIdioma(id, request);
    }

    public LiveData<BaseResponse<Void>> eliminarIdioma(int id) {
        return repository.eliminarIdioma(id);
    }
}