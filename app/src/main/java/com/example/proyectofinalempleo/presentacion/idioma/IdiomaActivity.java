package com.example.proyectofinalempleo.presentacion.idioma;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Idioma;
import com.example.proyectofinalempleo.presentacion.Perfil.PerfilActivity;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class IdiomaActivity extends AppCompatActivity {
    private IdiomaViewModel viewModel;
    private RecyclerView rvIdiomas;
    private IdiomaAdapter adapter;
    private FloatingActionButton btnAgregar;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_idioma);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(IdiomaViewModel.class);

        inicializarComponentes();
        recuperarIdYCargar();
        configurarMenuNavegacion();
    }
    private void inicializarComponentes() {
        rvIdiomas = findViewById(R.id.rv_idiomas);
        rvIdiomas.setLayoutManager(new LinearLayoutManager(this));

        adapter = new IdiomaAdapter(new IdiomaAdapter.OnIdiomaClickListener() {
            @Override
            public void onEditClick(Idioma idioma) {
                // Abrir formulario enviando datos para editar
                Intent intent = new Intent(IdiomaActivity.this, RegistroIdiomaActivity.class);
                intent.putExtra("ID_IDIOMA", idioma.getIdIdioma());
                intent.putExtra("NOMBRE_IDIOMA", idioma.getNombreIdioma());
                intent.putExtra("NIVEL", idioma.getNivel());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int id) {
                mostrarDialogoEliminar(id);
            }
        });

        rvIdiomas.setAdapter(adapter);

        btnAgregar = findViewById(R.id.btn_agregar_idioma);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroIdiomaActivity.class);
            startActivity(intent);
        });
    }
    private void recuperarIdYCargar() {
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            cargarDatos(idUsuario);
        }
    }
    private void cargarDatos(int idUsuario) {
        viewModel.obtenerIdiomas(idUsuario).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                adapter.setIdiomas(response.getData());
            } else {
                adapter.setIdiomas(new ArrayList<>());
            }
        });
    }
    private void mostrarDialogoEliminar(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar registro")
                .setMessage("¿Deseas eliminar este idioma?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viewModel.eliminarIdioma(id).observe(this, response -> {
                        if (response != null && response.isSuccess()) {
                            recuperarIdYCargar();
                            Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        recuperarIdYCargar();
    }
    private void configurarMenuNavegacion() {
        View btnInicio = findViewById(R.id.btnNavInicio);
        View btnPerfil = findViewById(R.id.btnNavPerfil);
        // Agregar btnNavEmpleo si lo tienes en el XML de estudio
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
}