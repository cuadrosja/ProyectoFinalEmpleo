package com.example.proyectofinalempleo.presentacion.experiencia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.ExperienciaLaboral;
import com.example.proyectofinalempleo.data.request.ExperienciaLaboralRequest;
import com.example.proyectofinalempleo.data.repository.ExperienciaLaboralRepository;

import java.util.List;

public class ExperienciaLaboralViewModel extends ViewModel {
    private final ExperienciaLaboralRepository repository;
    public ExperienciaLaboralViewModel() {
        this.repository = new ExperienciaLaboralRepository();
    }
    // Método para Obtener Experiencias
    public LiveData<BaseResponse<List<ExperienciaLaboral>>> obtenerExperiencias(int idUsuario) {
        return repository.listarExperiencia(idUsuario);
    }
    // Método para Registrar Experiencia
    public LiveData<BaseResponse<ExperienciaLaboral>> registrarExperiencia(ExperienciaLaboralRequest request) {
        return repository.registrarExperiencia(request);
    }
    // Método para Actualizar
    public LiveData<BaseResponse<ExperienciaLaboral>> actualizarExperiencia(int id, ExperienciaLaboralRequest request) {
        return repository.actualizarExperiencia(id, request);
    }
    // Método para Eliminar
    public LiveData<BaseResponse<Void>> eliminarExperiencia(int id) {
        return repository.eliminarExperiencia(id);
    }
}