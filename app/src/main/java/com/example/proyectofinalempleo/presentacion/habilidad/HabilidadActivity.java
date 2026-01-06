package com.example.proyectofinalempleo.presentacion.habilidad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Habilidad;
import com.example.proyectofinalempleo.data.request.HabilidadRequest;
import com.example.proyectofinalempleo.presentacion.Perfil.PerfilActivity;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HabilidadActivity extends AppCompatActivity {
    private HabilidadViewModel viewModel;
    private ChipGroup cgHabilidades;
    private FloatingActionButton btnAgregar;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";
    private int idUsuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habilidad);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(HabilidadViewModel.class);

        inicializarComponentes();
        recuperarIdYCargar();
        configurarMenuNavegacion();
    }
    private void inicializarComponentes() {
        cgHabilidades = findViewById(R.id.cg_habilidades);
        btnAgregar = findViewById(R.id.btn_agregar_habilidad);

        // ANTES: Se abría un cuadro de diálogo rápido para agregar.
        // CAMBIO: Se migró a RegistroHabilidadActivity para estandarizar
        //btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(HabilidadActivity.this, RegistroHabilidadActivity.class);
            startActivity(intent);
        });
    }
    private void recuperarIdYCargar() {
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        idUsuarioActual = sp.getInt("id_usuario", -1);

        if (idUsuarioActual != -1) {
            cargarDatos(idUsuarioActual);
        }
    }
    private void cargarDatos(int idUsuario) {
        viewModel.obtenerHabilidades(idUsuario).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                llenarChipGroup(response.getData());
            } else {
                cgHabilidades.removeAllViews();
            }
        });
    }
    private void llenarChipGroup(List<Habilidad> habilidades) {
        cgHabilidades.removeAllViews();
        for (Habilidad hab : habilidades) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_habilidad, cgHabilidades, false);
            chip.setText(hab.getNombreHabilidad());
            chip.setOnClickListener(v -> {
                Intent intent = new Intent(HabilidadActivity.this, RegistroHabilidadActivity.class);
                intent.putExtra("ID_HABILIDAD", hab.getIdHabilidad());
                intent.putExtra("NOMBRE_HABILIDAD", hab.getNombreHabilidad());
                startActivity(intent);
            });
            chip.setOnCloseIconClickListener(v -> mostrarDialogoEliminar(hab.getIdHabilidad()));
            cgHabilidades.addView(chip);
        }
    }

    // DISEÑO ALTERNATIVO (Dialog con Márgenes):
    /*private void mostrarDialogoAgregar() {
        // 1. Crear el contenedor (el "marco")
        android.widget.FrameLayout container = new android.widget.FrameLayout(this);

        // 2. Crear el campo de texto
        EditText input = new EditText(this);
        input.setHint("Ej. Java, Liderazgo, Excel");

        // 3. Configurar los márgenes para que no toque los bordes
        android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Convertimos 20dp a píxeles según la pantalla del celular
        int marginInDp = (int) (20 * getResources().getDisplayMetrics().density);
        params.leftMargin = marginInDp;
        params.rightMargin = marginInDp;

        input.setLayoutParams(params);
        container.addView(input); // Metemos el input dentro del marco

        // 4. Armar el diálogo usando el contenedor
        new AlertDialog.Builder(this)
                .setTitle("Nueva Habilidad")
                .setMessage("Ingresa el nombre de la habilidad:")
                .setView(container) // <--- Ahora pasamos el marco con márgenes
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = input.getText().toString().trim();
                    if (!nombre.isEmpty()) {
                        HabilidadRequest request = new HabilidadRequest(idUsuarioActual, nombre);
                        viewModel.registrarHabilidad(request).observe(this, response -> {
                            if (response != null && response.isSuccess()) {
                                cargarDatos(idUsuarioActual);
                                Toast.makeText(this, "Habilidad agregada", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }*/

    // Método original de creacion rapida de DISEÑO ALTERNATIVO (Dialog con Márgenes):
    /*private void mostrarDialogoAgregar() {
        EditText input = new EditText(this);
        input.setHint("Ej. Java, Liderazgo, Excel");

        new AlertDialog.Builder(this)
                .setTitle("Nueva Habilidad")
                .setMessage("Ingresa el nombre de la habilidad o conocimiento:")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = input.getText().toString().trim();
                    if (!nombre.isEmpty()) {
                        HabilidadRequest request = new HabilidadRequest(idUsuarioActual, nombre);
                        viewModel.registrarHabilidad(request).observe(this, response -> {
                            if (response != null && response.isSuccess()) {
                                cargarDatos(idUsuarioActual);
                                Toast.makeText(this, "Habilidad agregada", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }*/

    private void mostrarDialogoEliminar(int idHabilidad) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar habilidad")
                .setMessage("¿Estás seguro de quitar esta habilidad de tu perfil?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viewModel.eliminarHabilidad(idHabilidad).observe(this, response -> {
                        if (response != null && response.isSuccess()) {
                            cargarDatos(idUsuarioActual);
                            Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void configurarMenuNavegacion() {
        View btnInicio = findViewById(R.id.btnNavInicio);
        View btnPerfil = findViewById(R.id.btnNavPerfil);
        View btnEmpleo = findViewById(R.id.btnNavEmpleo);

        if (btnInicio != null) btnInicio.setOnClickListener(this::navegarMenu);
        if (btnPerfil != null) btnPerfil.setOnClickListener(this::navegarMenu);
        if (btnEmpleo != null) btnEmpleo.setOnClickListener(this::navegarMenu);
    }

    public void navegarMenu(View v) {
        int id = v.getId();
        Intent intent = null;

        if (id == R.id.btnNavInicio) intent = new Intent(this, InicioActivity.class);
        else if (id == R.id.btnNavEmpleo) intent = new Intent(this, EmpleoActivity.class);
        else if (id == R.id.btnNavPerfil) intent = new Intent(this, PerfilActivity.class);

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recuperarIdYCargar();
    }
}