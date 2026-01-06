package com.example.proyectofinalempleo.presentacion.estudio;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
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
import com.example.proyectofinalempleo.data.request.EstudioRequest;
import com.example.proyectofinalempleo.presentacion.common.Validator;

import java.util.Calendar;
import java.util.Locale;

public class RegistroEstudioActivity extends AppCompatActivity {

    private TextView txtTituloPantalla;
    private EditText editInstitucion, editTitulo, editFechaInicio, editFechaFin;
    private Spinner spinnerEstado;
    private Button btnGuardar;
    private EstudioViewModel viewModel;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";
    private int idEstudioEdicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_estudio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(EstudioViewModel.class);

        vincularComponentes();
        configurarSpinner();
        configurarFechas();
        verificarSiEsEdicion();

        btnGuardar.setOnClickListener(v -> ejecutarRegistro());
    }

    private void vincularComponentes() {
        txtTituloPantalla = findViewById(R.id.txtTitulo_estudio);
        editInstitucion = findViewById(R.id.edit_institucion);
        editTitulo = findViewById(R.id.edit_titulo);
        editFechaInicio = findViewById(R.id.edit_fecha_inicio_est);
        editFechaFin = findViewById(R.id.edit_fecha_fin_est);
        btnGuardar = findViewById(R.id.btn_guardar_estudio);

        spinnerEstado = findViewById(R.id.spinner_estado);
    }
    private void configurarSpinner() {
        String[] opciones = {"En curso", "Graduado", "Trunco", "Licenciado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinnerEstado.setAdapter(adapter);
    }

    private void verificarSiEsEdicion() {
        if (getIntent().hasExtra("ID_ESTUDIO")) {
            // 1. Recuperar el ID del estudio para saber que es una actualización
            idEstudioEdicion = getIntent().getIntExtra("ID_ESTUDIO", 0);

            editInstitucion.setText(getIntent().getStringExtra("INSTITUCION"));
            editTitulo.setText(getIntent().getStringExtra("TITULO"));
            editFechaInicio.setText(getIntent().getStringExtra("INICIO"));
            editFechaFin.setText(getIntent().getStringExtra("FIN"));

            String estadoVal = getIntent().getStringExtra("ESTADO");
            if (estadoVal != null && spinnerEstado.getAdapter() != null) {
                ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinnerEstado.getAdapter();
                int spinnerPosition = myAdap.getPosition(estadoVal);

                // Si el valor existe en la lista del spinner, lo selecciona
                if (spinnerPosition >= 0) {
                    spinnerEstado.setSelection(spinnerPosition);
                }
            }
            if (txtTituloPantalla != null) txtTituloPantalla.setText("Editar Formación");
            btnGuardar.setText("ACTUALIZAR");
        } else {
            if (txtTituloPantalla != null) txtTituloPantalla.setText("Añadir Formación");
            btnGuardar.setText("GUARDAR");
        }
    }
    private void configurarFechas() {
        // AGREGAR: DatePicker igual al de experiencia
        View.OnClickListener dateListener = v -> {
            final Calendar c = Calendar.getInstance();
            EditText et = (EditText) v;
            new DatePickerDialog(this, (view, y, m, d) -> {
                et.setText(String.format(Locale.US, "%02d/%02d/%04d", d, (m + 1), y));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        };

        editFechaInicio.setOnClickListener(dateListener);
        editFechaFin.setOnClickListener(dateListener);
    }

    private void ejecutarRegistro() {
        boolean esValido = Validator.with(editInstitucion).required().validate() &&
                Validator.with(editTitulo).required().validate() &&
                Validator.with(editFechaInicio).required().validate();
        if (!esValido) return;
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);
        if (idUsuario == -1) {
            Toast.makeText(this, "Sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }
        EstudioRequest request = new EstudioRequest();
        request.setIdUsuario(idUsuario);
        request.setInstitucion(editInstitucion.getText().toString().trim());
        request.setTituloObtenido(editTitulo.getText().toString().trim());
        request.setFechaInicio(editFechaInicio.getText().toString().trim());
        request.setFechaFin(editFechaFin.getText().toString().trim());

        request.setEstado(spinnerEstado.getSelectedItem().toString());
        if (idEstudioEdicion == 0) {
            viewModel.registrarEstudio(request).observe(this, response ->
                    manejarRespuesta(response, "Estudio guardado"));
        } else {
            viewModel.actualizarEstudio(idEstudioEdicion, request).observe(this, response ->
                    manejarRespuesta(response, "Estudio actualizado"));
        }
    }
    private void manejarRespuesta(BaseResponse<?> response, String msg) {
        if (response != null && response.isSuccess()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String errorMsg = (response != null) ? response.getMessage() : "Fallo de red";
            Toast.makeText(this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
        }
    }
    public void onClickSalirEstudio(View v) {
        finish();
    }
}