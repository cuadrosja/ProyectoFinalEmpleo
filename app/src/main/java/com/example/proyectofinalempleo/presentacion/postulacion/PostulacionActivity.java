package com.example.proyectofinalempleo.presentacion.postulacion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.proyectofinalempleo.presentacion.Perfil.PerfilActivity;
import com.example.proyectofinalempleo.presentacion.detalle.DetalleActivity;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;
import com.example.proyectofinalempleo.presentacion.postulacion.PostulacionViewModel;
import java.util.ArrayList;

public class PostulacionActivity extends AppCompatActivity {

    private RecyclerView rvPostulaciones;
    private ListaPostulacion adaptador;
    private PostulacionViewModel postulacionViewModel;
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_postulacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Llamamos a los métodos organizadores
        inicializarComponentes();
        cargarMisPostulaciones();
        configurarMenuNavegacion();
    }
    private void inicializarComponentes() {
        rvPostulaciones = findViewById(R.id.rv_postulaciones);
        rvPostulaciones.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new ListaPostulacion(new ArrayList<>(), postulacion -> {
            Intent intent = new Intent(this, DetalleActivity.class);
            intent.putExtra("ID_POSTULACION", postulacion.getIdPostulacion());
            if (postulacion.getEmpleo() != null) {
                intent.putExtra("ID_EMPLEO", postulacion.getEmpleo().getIdEmpleo());
                intent.putExtra("TITULO", postulacion.getEmpleo().getTituloEmpleo());
                intent.putExtra("DESCRIPCION", postulacion.getEmpleo().getDescripcion());
                intent.putExtra("FECHA", postulacion.getEmpleo().getFechaPublicacion());
                intent.putExtra("MODALIDAD", postulacion.getEmpleo().getModalidad());
                if (postulacion.getEmpleo().getEmpresa() != null) {
                    intent.putExtra("EMPRESA", postulacion.getEmpleo().getEmpresa().getNombreEmpresa());
                    intent.putExtra("DIRECCION", postulacion.getEmpleo().getEmpresa().getDireccion());
                    intent.putExtra("LOGO_URL", postulacion.getEmpleo().getEmpresa().getLogoUrl());
                }
                if (postulacion.getEmpleo().getCategoria() != null) {
                    intent.putExtra("CATEGORIA", postulacion.getEmpleo().getCategoria().getNombreCategoria());
                }
            }
            intent.putExtra("ORIGEN", "MIS_POSTULACIONES");
            startActivity(intent);
        });

        rvPostulaciones.setAdapter(adaptador);
        postulacionViewModel = new ViewModelProvider(this).get(PostulacionViewModel.class);
    }
    private void cargarMisPostulaciones() {
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            postulacionViewModel.obtenerMisPostulaciones(idUsuario).observe(this, response -> {
                if (response != null && response.isSuccess()) {
                    adaptador.setPostulaciones(response.getData());
                } else {
                    Toast.makeText(this, "No tienes postulaciones aún", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show();
        }
    }
    private void configurarMenuNavegacion() {
        // 1. Buscamos los botones por su ID
        Button btnNavInicio = findViewById(R.id.btnNavInicio);
        Button btnNavEmpleo = findViewById(R.id.btnNavEmpleo);
        Button btnNavPerfil = findViewById(R.id.btnNavPerfil);

        // 2. Les asignamos el mismo comportamiento de navegación
        if (btnNavInicio != null) btnNavInicio.setOnClickListener(this::navegarMenu);
        if (btnNavEmpleo != null) btnNavEmpleo.setOnClickListener(this::navegarMenu);
        if (btnNavPerfil != null) btnNavPerfil.setOnClickListener(this::navegarMenu);
        if (btnNavPerfil != null) btnNavPerfil.setSelected(true);
    }

    public void navegarMenu(View v) {
        int id = v.getId();
        Intent intent = null;

        if (id == R.id.btnNavInicio) {
            intent = new Intent(this, InicioActivity.class);
        } else if (id == R.id.btnNavEmpleo) {
            intent = new Intent(this, EmpleoActivity.class);
        } else if (id == R.id.btnNavPerfil) {
            intent = new Intent(this, PerfilActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarMisPostulaciones();
    }
}