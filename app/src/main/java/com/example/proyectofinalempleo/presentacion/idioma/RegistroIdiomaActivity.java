package com.example.proyectofinalempleo.presentacion.idioma;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.proyectofinalempleo.data.request.IdiomaRequest;
import com.example.proyectofinalempleo.presentacion.common.Validator;

public class RegistroIdiomaActivity extends AppCompatActivity {
    private TextView txtTituloPantalla;
    private EditText editNombreIdioma;
    private Spinner spinnerNivel;
    private Button btnGuardar, btnCancelar;
    private IdiomaViewModel viewModel;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";
    private int idIdiomaEdicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_idioma);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(IdiomaViewModel.class);

        vincularComponentes();
        configurarSpinner();
        verificarSiEsEdicion();

        btnGuardar.setOnClickListener(v -> ejecutarRegistro());
        btnCancelar.setOnClickListener(v -> finish());
    }
    private void vincularComponentes() {
        txtTituloPantalla = findViewById(R.id.txtTitulo_idioma);
        editNombreIdioma = findViewById(R.id.edit_nombre_idioma);
        spinnerNivel = findViewById(R.id.spinner_nivel_idioma);
        btnGuardar = findViewById(R.id.btn_guardar_idioma);
        btnCancelar = findViewById(R.id.btn_cancelar_idioma);
    }
    private void configurarSpinner() {
        // Opciones de nivel profesional
        String[] opciones = {"Básico", "Intermedio", "Avanzado", "Nativo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, opciones);
        spinnerNivel.setAdapter(adapter);
    }
    private void verificarSiEsEdicion() {
        if (getIntent().hasExtra("ID_IDIOMA")) {
            idIdiomaEdicion = getIntent().getIntExtra("ID_IDIOMA", 0);
            editNombreIdioma.setText(getIntent().getStringExtra("NOMBRE_IDIOMA"));

            String nivelSeleccionado = getIntent().getStringExtra("NIVEL");
            if (nivelSeleccionado != null && spinnerNivel.getAdapter() != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerNivel.getAdapter();
                int position = adapter.getPosition(nivelSeleccionado);
                if (position >= 0) spinnerNivel.setSelection(position);
            }
            if (txtTituloPantalla != null) {
                txtTituloPantalla.setText("Editar Idioma");
            }
            btnGuardar.setText("ACTUALIZAR");
        }
    }
    private void ejecutarRegistro() {
        // Uso de tu clase Validator igual que en Estudios
        boolean esValido = Validator.with(editNombreIdioma).required().validate();
        if (!esValido) return;

        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        IdiomaRequest request = new IdiomaRequest();
        request.setIdUsuario(idUsuario);
        request.setNombreIdioma(editNombreIdioma.getText().toString().trim());
        request.setNivel(spinnerNivel.getSelectedItem().toString());

        if (idIdiomaEdicion == 0) {
            viewModel.registrarIdioma(request).observe(this, response ->
                    manejarRespuesta(response, "Idioma guardado"));
        } else {
            // Asumiendo que tienes actualizarIdioma en el ViewModel
            viewModel.actualizarIdioma(idIdiomaEdicion, request).observe(this, response ->
                    manejarRespuesta(response, "Idioma actualizado"));
        }
    }
    private void manejarRespuesta(BaseResponse<?> response, String msg) {
        if (response != null && response.isSuccess()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String errorMsg = (response != null) ? response.getMessage() : "Error de conexión";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        }
    }
    public void onClickSalirIdioma(View v) {
        finish();
    }
}