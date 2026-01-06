package com.example.proyectofinalempleo.presentacion.estudio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Estudio;
import com.example.proyectofinalempleo.data.request.EstudioRequest;
import com.example.proyectofinalempleo.data.repository.EstudioRepository;

import java.util.List;

public class EstudioViewModel extends ViewModel {
    private final EstudioRepository repository;

    public EstudioViewModel() {
        this.repository = new EstudioRepository();
    }

    public LiveData<BaseResponse<List<Estudio>>> obtenerEstudios(int idUsuario) {
        return repository.listarEstudios(idUsuario);
    }

    public LiveData<BaseResponse<Estudio>> registrarEstudio(EstudioRequest request) {
        return repository.registrarEstudio(request);
    }

    public LiveData<BaseResponse<Estudio>> actualizarEstudio(int id, EstudioRequest request) {
        return repository.actualizarEstudio(id, request);
    }

    public LiveData<BaseResponse<Void>> eliminarEstudio(int id) {
        return repository.eliminarEstudio(id);
    }
}