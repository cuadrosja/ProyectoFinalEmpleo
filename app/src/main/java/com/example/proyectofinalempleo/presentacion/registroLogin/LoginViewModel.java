package com.example.proyectofinalempleo.presentacion.registroLogin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.model.Usuario;
import com.example.proyectofinalempleo.data.repository.UsuarioRepository;
import com.example.proyectofinalempleo.data.request.LoginUsuarioRequest;
import com.example.proyectofinalempleo.data.request.RegistroRequest;

public class LoginViewModel extends ViewModel{
    private final UsuarioRepository usuarioRepository;

    public LoginViewModel() {
        usuarioRepository = new UsuarioRepository();
    }

    public LiveData<BaseResponse<Usuario>> obtenerUsuarioPorIdUsuario(int id) {
        return usuarioRepository.obtenerUsuarioPorIdUsuario(id);
    }
    public LiveData<BaseResponse<Usuario>> inicioSesion(LoginUsuarioRequest request) {
        return usuarioRepository.inicioSesion(request);
    }
    public LiveData<BaseResponse<Usuario>> registrarUsuario(RegistroRequest request) {
        return usuarioRepository.registrarUsuario(request);
    }
    public LiveData<BaseResponse<Usuario>> actualizarUsuario(int id, RegistroRequest request) {
        return usuarioRepository.actualizarUsuario(id, request);
    }
}
