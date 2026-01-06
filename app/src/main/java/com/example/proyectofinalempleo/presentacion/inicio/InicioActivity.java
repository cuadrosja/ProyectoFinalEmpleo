package com.example.proyectofinalempleo.presentacion.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.content.Context;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Usuario;
import com.example.proyectofinalempleo.data.request.LoginUsuarioRequest;
import com.example.proyectofinalempleo.presentacion.common.Validator;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.registroLogin.LoginViewModel;
import com.example.proyectofinalempleo.presentacion.registroLogin.RegistroActivity;

public class InicioActivity extends AppCompatActivity {

    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";
    private static final String CLAVE_ESTADO_SESION = "estadoSesión";
    private static final String CLAVE_ID_USUARIO = "id_usuario";

    private EditText editUsuario;
    private EditText editPassword;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editUsuario = findViewById(R.id.edit_usuario);
        editPassword = findViewById(R.id.edit_password);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        verificarEstadoSesion();
    }
    public void onClickIniciarSesion(View v) {

        if (!Validator.with(editUsuario).required().validate()) return;
        if (!Validator.with(editPassword).required().validate()) return;

        String username = editUsuario.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        LoginUsuarioRequest request = new LoginUsuarioRequest();
        request.setNombreUsuario(username);
        request.setContrasena(password);

        loginViewModel.inicioSesion(request).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Usuario usuario = response.getData();

                String getNombre = (usuario.getNombre() != null) ? usuario.getNombre() : usuario.getNombreUsuario();
                SharedPreferences sharedPref = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean(CLAVE_ESTADO_SESION, true);
                editor.putInt(CLAVE_ID_USUARIO, usuario.getIdUsuario());
                editor.putString("nombre", usuario.getNombre());
                editor.putString("apellidos", usuario.getApellidos());
                editor.putString("email", usuario.getEmail());
                editor.putString("telefono", usuario.getTelefono());
                editor.putString("fotoPerfil", usuario.getFotoPerfil());
                editor.apply();

                Toast.makeText(this, "Bienvenido(a), " + getNombre, Toast.LENGTH_LONG).show();
                navegaPantallaPrincipal();

            } else if (response != null) {
                Toast.makeText(this, "Error de ingreso: " + response.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Fallo de conexión. Intente más tarde.", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void verificarEstadoSesion() {
        SharedPreferences sharedPref = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        boolean sesionIniciada = sharedPref.getBoolean(CLAVE_ESTADO_SESION, false);
        if (sesionIniciada) {
            Toast.makeText(this, "Sesión activa. Redirigiendo...", Toast.LENGTH_SHORT).show();
            navegaPantallaPrincipal();
        }
    }
    private void navegaPantallaPrincipal() {
        Intent intent = new Intent(this, EmpleoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void onClickRegistrarse(View v){
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
    }
    public void onClickMostrarEmpleo(View v){
        Intent i = new Intent(this, EmpleoActivity.class);
        startActivity(i);
    }
}