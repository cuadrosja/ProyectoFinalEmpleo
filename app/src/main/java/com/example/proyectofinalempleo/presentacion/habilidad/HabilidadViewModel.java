package com.example.proyectofinalempleo.presentacion.habilidad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Habilidad;
import com.example.proyectofinalempleo.data.request.HabilidadRequest;
import com.example.proyectofinalempleo.data.repository.HabilidadRepository;

import java.util.List;

public class HabilidadViewModel extends ViewModel {
    private final HabilidadRepository repository;

    public HabilidadViewModel() {
        this.repository = new HabilidadRepository();
    }

    public LiveData<BaseResponse<List<Habilidad>>> obtenerHabilidades(int idUsuario) {
        return repository.listarHabilidades(idUsuario);
    }

    public LiveData<BaseResponse<Habilidad>> registrarHabilidad(HabilidadRequest request) {
        return repository.registrarHabilidad(request);
    }
    public LiveData<BaseResponse<Habilidad>> actualizarHabilidad(int id, HabilidadRequest request) {
        return repository.actualizarHabilidad(id, request);
    }
    public LiveData<BaseResponse<Void>> eliminarHabilidad(int id) {
        return repository.eliminarHabilidad(id);
    }
}