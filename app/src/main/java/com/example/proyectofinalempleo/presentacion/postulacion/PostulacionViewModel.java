package com.example.proyectofinalempleo.presentacion.postulacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Postulacion;
import com.example.proyectofinalempleo.data.repository.PostulacionRepository;
import com.example.proyectofinalempleo.data.request.PostulacionRequest;

import java.util.List;

public class PostulacionViewModel extends ViewModel {
    private final PostulacionRepository repository;
    public PostulacionViewModel() {
        this.repository = new PostulacionRepository();
    }
    public LiveData<BaseResponse<Postulacion>> registrarPostulacion(PostulacionRequest request) {
        return repository.registrarPostulacion(request);
    }
    public LiveData<BaseResponse<List<Postulacion>>> obtenerMisPostulaciones(int idUsuario) {
        return repository.obtenerMisPostulaciones(idUsuario);
    }
    public LiveData<BaseResponse<Void>> cancelarPostulacion(int idPostulacion) {
        return repository.cancelarPostulacion(idPostulacion);
    }
}