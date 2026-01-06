package com.example.proyectofinalempleo.presentacion.registroLogin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.request.RegistroRequest;
import com.example.proyectofinalempleo.presentacion.common.Validator;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class RegistroActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private EditText editNombre;
    private EditText editApellido;
    private EditText editEmail;
    private EditText editTelefono;
    private EditText editUsuario;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editNombre = findViewById(R.id.edit_nombre);
        editApellido = findViewById(R.id.edit_apellido);
        editEmail = findViewById(R.id.edit_email_reg);
        editTelefono = findViewById(R.id.edit_telefono);
        editUsuario = findViewById(R.id.edit_usuario_reg);
        editPassword = findViewById(R.id.edit_password_reg);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    public void onClickCrearCuenta(View v) {
        if (!Validator.with(editNombre).required().validate()) return;
        if (!Validator.with(editApellido).required().validate()) return;
        if (!Validator.with(editEmail).required().email().validate()) return;
        if (!Validator.with(editUsuario).required().validate()) return;
        if (!Validator.with(editPassword).required().validate()) return;

        RegistroRequest registroRequest = new RegistroRequest();

        registroRequest.setNombre(editNombre.getText().toString());
        registroRequest.setApellidos(editApellido.getText().toString());
        registroRequest.setEmail(editEmail.getText().toString());
        registroRequest.setTelefono(editTelefono.getText().toString());
        registroRequest.setNombreUsuario(editUsuario.getText().toString());
        registroRequest.setContrasena(editPassword.getText().toString());

        loginViewModel.registrarUsuario(registroRequest).observe(this, response -> {

            if (!response.isSuccess()) {
                Toast.makeText(this, "ERROR: " + response.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "¡Registro Exitoso! Inicie Sesion,  "
                        + response.getData().getNombre(),Toast.LENGTH_LONG).show();

                // Navegar a la pantalla de Login para que el usuario ingrese
                Intent intent = new Intent(this, InicioActivity.class);
                startActivity(intent);
                this.finish(); // Cierra RegistroActivity
            }
        });
    }
    public void onClickCancelarRegistro(View v) {
        // Creamos la alerta de confirmación
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Limpiar formulario")
                .setMessage("¿Estás seguro de que quieres borrar todos los datos ingresados?")
                .setCancelable(false)
                .setPositiveButton("Sí, limpiar", (dialog, which) -> {
                    // Si el usuario confirma, llamamos al método que borra todo
                    ejecutarLimpieza();
                })
                .setNegativeButton("No, cancelar", (dialog, which) -> {
                    // Si dice que no, solo cerramos la alerta
                    dialog.dismiss();
                })
                .show();
    }

    // Este es el nuevo método que hace el trabajo sucio de borrar los textos
    private void ejecutarLimpieza() {
        editNombre.setText("");
        editApellido.setText("");
        editEmail.setText("");
        editTelefono.setText("");
        editUsuario.setText("");
        editPassword.setText("");

        // Limpiamos los mensajes de error rojos del validador
        editNombre.setError(null);
        editApellido.setError(null);
        editEmail.setError(null);
        editTelefono.setError(null);
        editUsuario.setError(null);
        editPassword.setError(null);

        // Ponemos el cursor en el primer campo
        editNombre.requestFocus();

        Toast.makeText(this, "Formulario reiniciado", Toast.LENGTH_SHORT).show();
    }
}