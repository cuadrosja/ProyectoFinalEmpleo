package com.example.proyectofinalempleo.presentacion.habilidad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.common.BaseResponse;
import com.example.proyectofinalempleo.data.request.HabilidadRequest;
import com.example.proyectofinalempleo.presentacion.common.Validator;

public class RegistroHabilidadActivity extends AppCompatActivity {
    private TextView txtTituloPantalla;
    private EditText editNombreHabilidad;
    private Button btnGuardar, btnCancelar;
    private HabilidadViewModel viewModel;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";
    private int idHabilidadEdicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_habilidad);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(HabilidadViewModel.class);

        vincularComponentes();
        verificarSiEsEdicion(); // <--- Clave para cargar datos si venimos de la lista

        btnGuardar.setOnClickListener(v -> ejecutarRegistro());
        btnCancelar.setOnClickListener(v -> finish());
    }
    private void vincularComponentes() {
        // Vinculación con los IDs de tu archivo XML
        txtTituloPantalla = findViewById(R.id.txtTitulo_registro_hab);
        editNombreHabilidad = findViewById(R.id.edit_nombre_habilidad);
        btnGuardar = findViewById(R.id.btn_guardar_habilidad);
        btnCancelar = findViewById(R.id.btn_cancelar_habilidad);
    }
    private void verificarSiEsEdicion() {
        if (getIntent().hasExtra("ID_HABILIDAD")) {
            idHabilidadEdicion = getIntent().getIntExtra("ID_HABILIDAD", 0);
            editNombreHabilidad.setText(getIntent().getStringExtra("NOMBRE_HABILIDAD"));

            if (txtTituloPantalla != null) {
                txtTituloPantalla.setText("Editar Habilidad");
            }
            btnGuardar.setText("ACTUALIZAR");

        } else {
            if (txtTituloPantalla != null) txtTituloPantalla.setText("Añadir Habilidad");
            btnGuardar.setText("GUARDAR");
        }
    }
    private void ejecutarRegistro() {
        boolean esValido = Validator.with(editNombreHabilidad).required().validate();
        if (!esValido) return;
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = editNombreHabilidad.getText().toString().trim();
        HabilidadRequest request = new HabilidadRequest(idUsuario, nombre);
        if (idHabilidadEdicion == 0) {
            viewModel.registrarHabilidad(request).observe(this, response ->
                    manejarRespuesta(response, "Habilidad guardada correctamente"));
        } else {
            // Se asume que el ViewModel tiene el método para actualizar habilidades
            viewModel.actualizarHabilidad(idHabilidadEdicion, request).observe(this, response ->
                    manejarRespuesta(response, "Habilidad actualizada correctamente"));
        }
    }
    private void manejarRespuesta(BaseResponse<?> response, String msg) {
        if (response != null && response.isSuccess()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String errorMsg = (response != null) ? response.getMessage() : "Error de conexión";
            Toast.makeText(this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
        }
    }
    public void onClickSalirHabilidad(View v) {
        finish();
    }
}