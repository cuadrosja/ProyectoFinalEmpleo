package com.example.proyectofinalempleo.presentacion.empleo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Empleo;
import com.example.proyectofinalempleo.data.repository.EmpleoRepository;
import java.util.List;

public class EmpleoViewModel extends ViewModel {
    private final EmpleoRepository repository;
    public EmpleoViewModel() {
        this.repository = new EmpleoRepository();
    }
    public LiveData<BaseResponse<List<Empleo>>> obtenerEmpleos(int limite,int pagina, String busqueda, String ubicacion) {
        return repository.listarEmpleos(limite, pagina, busqueda, ubicacion);
    }
}