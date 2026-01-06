package com.example.proyectofinalempleo.presentacion.experiencia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.presentacion.Perfil.PerfilActivity;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;
import com.example.proyectofinalempleo.data.model.ExperienciaLaboral;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExperienciaLaboralActivity extends AppCompatActivity {

    private ExperienciaLaboralViewModel viewModel;
    private RecyclerView rvExperiencia;
    private ListaExperienciaLaboral adapter;
    private FloatingActionButton btnAgregar;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experiencia_laboral);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(ExperienciaLaboralViewModel.class);
        // 1. Inicializar componentes
        inicializarComponentes();

        // 2. Cargar datos iniciales
        recuperarIdYCargar();

        // 3. Configurar navegación (si usas el include de navegación)
        configurarMenuNavegacion();
    }
    private void inicializarComponentes() {
        rvExperiencia = findViewById(R.id.rv_experiencia_laboral);
        rvExperiencia.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListaExperienciaLaboral(new ArrayList<>(), new ListaExperienciaLaboral.OnItemClickListener() {
            @Override
            public void onEditClick(ExperienciaLaboral experiencia) {
                // Abrir Registro enviando el objeto para editar
                Intent intent = new Intent(ExperienciaLaboralActivity.this, RegistroExperienciaActivity.class);
                intent.putExtra("ID_EXP", experiencia.getIdExperiencia());
                intent.putExtra("EMPRESA", experiencia.getNombreEmpresa());
                intent.putExtra("PUESTO", experiencia.getPuestoOcupado());
                intent.putExtra("DESC", experiencia.getDescripcionTareas());
                intent.putExtra("INICIO", experiencia.getFechaInicio());
                intent.putExtra("FIN", experiencia.getFechaFin());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int id) {
                // Confirmar antes de borrar
                mostrarDialogoEliminar(id);
            }
        });

        rvExperiencia.setAdapter(adapter);

        btnAgregar = findViewById(R.id.btn_agregar_exp);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroExperienciaActivity.class);
            startActivity(intent);
        });
    }
    private void mostrarDialogoEliminar(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Registro")
                .setMessage("¿Estás seguro de que deseas eliminar esta experiencia laboral?")
                .setPositiveButton("Eliminar", (dialog, which) -> ejecutarEliminacion(id))
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void ejecutarEliminacion(int id) {
        viewModel.eliminarExperiencia(id).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                recuperarIdYCargar(); // Refrescar lista
            } else {
                Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void recuperarIdYCargar() {
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            cargarDatos(idUsuario);
        } else {
            Toast.makeText(this, "Error: Sesión no válida", Toast.LENGTH_SHORT).show();
        }
    }
    private void cargarDatos(int idUsuario) {
        viewModel.obtenerExperiencias(idUsuario).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                adapter.setExperiencia(response.getData());
            } else {
                adapter.setExperiencia(new ArrayList<>()); // Lista vacía si falla o no hay datos
                String msg = (response != null) ? response.getMessage() : "Error al cargar datos";
                // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void configurarMenuNavegacion() {
        // Implementa aquí la lógica de botones si usas el mismo menú que en EmpleoActivity
        View btnInicio = findViewById(R.id.btnNavInicio);
        View btnEmpleo = findViewById(R.id.btnNavEmpleo);
        View btnPerfil = findViewById(R.id.btnNavPerfil);

        if (btnInicio != null) btnInicio.setOnClickListener(this::navegarMenu);
        if (btnEmpleo != null) btnEmpleo.setOnClickListener(this::navegarMenu);
        if (btnPerfil != null) btnPerfil.setOnClickListener(this::navegarMenu);
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
        // Recargar datos por si el usuario agregó algo nuevo
        recuperarIdYCargar();
    }
}