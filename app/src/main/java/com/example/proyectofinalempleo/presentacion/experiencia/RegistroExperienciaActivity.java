package com.example.proyectofinalempleo.presentacion.experiencia;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.proyectofinalempleo.data.request.ExperienciaLaboralRequest;
import com.example.proyectofinalempleo.presentacion.common.Validator;

import java.util.Calendar;
import java.util.Locale;

public class RegistroExperienciaActivity extends AppCompatActivity {

    private TextView txtTituloPantalla;
    private EditText editEmpresa, editPuesto, editFechaInicio, editFechaFin, editDescripcion;
    private Button btnGuardar;
    private ExperienciaLaboralViewModel viewModel;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";

    // Variable para saber si estamos editando
    private int idExperienciaEdicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_experiencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(ExperienciaLaboralViewModel.class);

        vincularComponentes();
        configurarFechas();
        verificarSiEsEdicion();

        btnGuardar.setOnClickListener(v -> ejecutarRegistro());
    }

    private void vincularComponentes() {

        txtTituloPantalla = findViewById(R.id.txtTitulo_experiencia);

        editEmpresa = findViewById(R.id.edit_empresa);
        editPuesto = findViewById(R.id.edit_puesto);
        editFechaInicio = findViewById(R.id.edit_fecha_inicio);
        editFechaFin = findViewById(R.id.edit_fecha_fin);
        editDescripcion = findViewById(R.id.edit_descripcion);
        btnGuardar = findViewById(R.id.btn_guardar_experiencia);
    }

    private void verificarSiEsEdicion() {
        if (getIntent().hasExtra("ID_EXP")) {
            idExperienciaEdicion = getIntent().getIntExtra("ID_EXP", 0);
            editEmpresa.setText(getIntent().getStringExtra("EMPRESA"));
            editPuesto.setText(getIntent().getStringExtra("PUESTO"));
            editDescripcion.setText(getIntent().getStringExtra("DESC"));
            editFechaInicio.setText(getIntent().getStringExtra("INICIO"));
            editFechaFin.setText(getIntent().getStringExtra("FIN"));

            if (txtTituloPantalla != null) {
                txtTituloPantalla.setText("Editar Experiencia");
            }
            btnGuardar.setText("ACTUALIZAR");

        } else {
            // Asegurarse de que el estado por defecto sea "Añadir"
            if (txtTituloPantalla != null) {
                txtTituloPantalla.setText("Añadir Experiencia");
            }
            btnGuardar.setText("GUARDAR");
        }
    }
    public void onClickSalirExpLaboral(View v) {
        //startActivity(new Intent(this, com.example.proyectofinalempleo.presentacion.experiencia.ExperienciaLaboral.class));
        finish();
    }

    private void configurarFechas() {
        editFechaInicio.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                editFechaInicio.setText(String.format(Locale.US, "%02d/%02d/%04d", d, (m + 1), y));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        editFechaFin.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                editFechaFin.setText(String.format(Locale.US, "%02d/%02d/%04d", d, (m + 1), y));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void ejecutarRegistro() {
        boolean esValido = Validator.with(editEmpresa).required().validate() &&
                Validator.with(editPuesto).required().validate() &&
                Validator.with(editFechaInicio).required().validate() &&
                Validator.with(editFechaFin).required().validate();

        if (!esValido) return;

        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Error: Sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        ExperienciaLaboralRequest request = new ExperienciaLaboralRequest();
        request.setIdUsuario(idUsuario);
        request.setNombreEmpresa(editEmpresa.getText().toString().trim());
        request.setPuestoOcupado(editPuesto.getText().toString().trim());
        request.setFechaInicio(editFechaInicio.getText().toString().trim());
        request.setFechaFin(editFechaFin.getText().toString().trim());
        request.setDescripcionTareas(editDescripcion.getText().toString().trim());

        // --- Lógica Inteligente: ¿Guardar o Actualizar? ---
        if (idExperienciaEdicion == 0) {
            // Registrar nuevo (POST)
            viewModel.registrarExperiencia(request).observe(this, response -> {
                manejarRespuestaServidor(response, "¡Experiencia guardada!");
            });
        } else {
            // Actualizar existente (PUT)
            viewModel.actualizarExperiencia(idExperienciaEdicion, request).observe(this, response -> {
                manejarRespuestaServidor(response, "¡Experiencia actualizada!");
            });
        }
    }
    private void manejarRespuestaServidor(com.example.proyectofinalempleo.data.common.BaseResponse<?> response, String mensajeExito) {
        if (response != null && response.isSuccess()) {
            Toast.makeText(this, mensajeExito, Toast.LENGTH_SHORT).show();
            finish(); // Cierra y vuelve a la lista
        } else {
            String error = (response != null) ? response.getMessage() : "Error al conectar con el servidor";
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}