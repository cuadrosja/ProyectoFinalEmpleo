package com.example.proyectofinalempleo.presentacion.empleo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Empleo;
import com.example.proyectofinalempleo.presentacion.Perfil.PerfilActivity;
import com.example.proyectofinalempleo.presentacion.detalle.DetalleActivity;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoViewModel;

import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.ViewModelProvider;

public class EmpleoActivity extends AppCompatActivity {

    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";

    private RecyclerView rvEmpleos;
    private ListaEmpleo adaptador;
    private List<Empleo> listaDePrueba;
    private EmpleoViewModel empleoViewModel;

    //PAGINACIÓN ---
    private int paginaActual = 1;
    private final int limitePorPagina = 10;
    private TextView txtPaginaActual;
    private EditText campoBusqueda;
    private EditText campoUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empleo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        campoBusqueda = findViewById(R.id.edit_tipo_empleo);
        campoUbicacion = findViewById(R.id.edit_ubicacion);
        txtPaginaActual = findViewById(R.id.txt_pagina_actual);
        // Organización de la actividad
        inicializarComponentes();
        configurarMenuNavegacion();
        configurarBusqueda();
        configurarPaginacion();
    }

    private void inicializarComponentes() {
        // 1. ViewModel
        empleoViewModel = new ViewModelProvider(this).get(EmpleoViewModel.class);

        // 2. RecyclerView y Adaptador
        rvEmpleos = findViewById(R.id.rv_empleos);
        rvEmpleos.setLayoutManager(new LinearLayoutManager(this));

        listaDePrueba = new ArrayList<>();
        adaptador = new ListaEmpleo(listaDePrueba, empleo -> {
            Intent intent = new Intent(this, DetalleActivity.class);
            intent.putExtra("ID_EMPLEO", empleo.getIdEmpleo());
            intent.putExtra("TITULO", empleo.getTituloEmpleo());
            intent.putExtra("EMPRESA", empleo.getEmpresa().getNombreEmpresa());
            intent.putExtra("DESCRIPCION", empleo.getDescripcion());
            intent.putExtra("LOGO_URL", empleo.getEmpresa().getLogoUrl());

            intent.putExtra("MODALIDAD", empleo.getModalidad());
            intent.putExtra("DIRECCION", empleo.getEmpresa().getDireccion());
            intent.putExtra("CATEGORIA", empleo.getCategoria().getNombreCategoria());

            if (empleo.getFechaPublicacion() != null) {
                intent.putExtra("FECHA", empleo.getFechaPublicacion().toString());
            } else {
                intent.putExtra("FECHA", "Reciente");
            }
            startActivity(intent);
        });
        rvEmpleos.setAdapter(adaptador);
    }
    private void configurarPaginacion() {
        Button btnAnterior = findViewById(R.id.btn_anterior);
        Button btnSiguiente = findViewById(R.id.btn_siguiente);

        btnAnterior.setEnabled(false);

        btnAnterior.setOnClickListener(v -> {
            paginaActual--;
            ejecutarCargaConFiltros();
        });

        btnSiguiente.setOnClickListener(v -> {
            paginaActual++;
            ejecutarCargaConFiltros();
        });

        pedirDatosAlServidor("","");
    }
    private void ejecutarCargaConFiltros() {
        String entradaBusqueda = campoBusqueda.getText().toString().trim();
        String entradaUbicacion = campoUbicacion.getText().toString().trim();

        pedirDatosAlServidor(entradaBusqueda, entradaUbicacion);
    }
    private void configurarBusqueda() {
        Button btnBuscar = findViewById(R.id.btn_buscar);

        btnBuscar.setOnClickListener(v -> {
            paginaActual = 1;
            ejecutarCargaConFiltros();
        });
    }
    private void pedirDatosAlServidor(String terminoBusqueda, String terminoUbicacion) {
        txtPaginaActual.setText("Página " + paginaActual);
        Button btnAnterior = findViewById(R.id.btn_anterior);
        Button btnSiguiente = findViewById(R.id.btn_siguiente);

        // Llamada actualizada al ViewModel con 4 parámetros
        empleoViewModel.obtenerEmpleos(limitePorPagina, paginaActual, terminoBusqueda, terminoUbicacion)
                .observe(this, response -> {
                    if (response != null && response.isSuccess()) {
                        List<Empleo> nuevosDatos = response.getData();
                        if (nuevosDatos != null && !nuevosDatos.isEmpty()) {
                            listaDePrueba.clear();
                            listaDePrueba.addAll(nuevosDatos);
                            adaptador.setEmpleos(listaDePrueba);
                            rvEmpleos.scrollToPosition(0);

                            btnAnterior.setEnabled(paginaActual > 1);
                            btnSiguiente.setEnabled(nuevosDatos.size() == limitePorPagina);

                        } else {
                            manejarListaVacia(btnAnterior, btnSiguiente);
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void manejarListaVacia(Button btnAnterior, Button btnSiguiente) {
        if (paginaActual == 1) {
            listaDePrueba.clear();
            adaptador.setEmpleos(listaDePrueba);
            btnAnterior.setEnabled(false);
            btnSiguiente.setEnabled(false);
            Toast.makeText(this, "No se encontraron empleos", Toast.LENGTH_SHORT).show();
        } else {
            paginaActual--;
            txtPaginaActual.setText("Página " + paginaActual);
            btnSiguiente.setEnabled(false);
            Toast.makeText(this, "No hay más empleos", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarMenuNavegacion() {
        Button btnNavInicio = findViewById(R.id.btnNavInicio);
        Button btnNavEmpleo = findViewById(R.id.btnNavEmpleo);
        Button btnNavPerfil = findViewById(R.id.btnNavPerfil);

        if (btnNavInicio != null) btnNavInicio.setOnClickListener(this::navegarMenu);
        if (btnNavEmpleo != null) btnNavEmpleo.setOnClickListener(this::navegarMenu);
        if (btnNavEmpleo != null) btnNavEmpleo.setSelected(true);
        if (btnNavPerfil != null) btnNavPerfil.setOnClickListener(this::navegarMenu);
    }

    public void navegarMenu(View v) {
        int id = v.getId();
        if (id == R.id.btnNavEmpleo) return;
        Intent intent = null;
        if (id == R.id.btnNavInicio) {
            intent = new Intent(this, InicioActivity.class);
        } else if (id == R.id.btnNavPerfil) {
            intent = new Intent(this, PerfilActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }
}