package com.example.proyectofinalempleo.presentacion.favorito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.proyectofinalempleo.presentacion.detalle.DetalleActivity;
import com.example.proyectofinalempleo.presentacion.favorito.FavoritoViewModel;

import java.util.ArrayList;

public class FavoritoActivity extends AppCompatActivity {

    private ListaFavorito adapter;
    private FavoritoViewModel favoritoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorito);

        // Configuración de Insets para pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        cargarMisFavoritos();
    }

    private void inicializarComponentes() {
        favoritoViewModel = new ViewModelProvider(this).get(FavoritoViewModel.class);

        RecyclerView rv = findViewById(R.id.rvFavoritos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListaFavorito(new ArrayList<>(), favorito -> {
            Intent intent = new Intent(this, DetalleActivity.class);
            intent.putExtra("ID_FAVORITO", favorito.getIdFavorito());
            if (favorito.getEmpleo() != null) {
                intent.putExtra("ID_EMPLEO", favorito.getEmpleo().getIdEmpleo());
                intent.putExtra("TITULO", favorito.getEmpleo().getTituloEmpleo());
                intent.putExtra("DESCRIPCION", favorito.getEmpleo().getDescripcion());
                intent.putExtra("FECHA", favorito.getEmpleo().getFechaPublicacion());
                intent.putExtra("MODALIDAD", favorito.getEmpleo().getModalidad());

                if (favorito.getEmpleo().getEmpresa() != null) {
                    intent.putExtra("EMPRESA", favorito.getEmpleo().getEmpresa().getNombreEmpresa());
                    intent.putExtra("DIRECCION", favorito.getEmpleo().getEmpresa().getDireccion());
                    intent.putExtra("LOGO_URL", favorito.getEmpleo().getEmpresa().getLogoUrl());
                }
                if (favorito.getEmpleo().getCategoria() != null) {
                    intent.putExtra("CATEGORIA", favorito.getEmpleo().getCategoria().getNombreCategoria());
                }
            }
            intent.putExtra("ORIGEN", "MIS_FAVORITOS");
            startActivity(intent);
        });

        rv.setAdapter(adapter);
    }

    private void cargarMisFavoritos() {
        // Obtener ID de usuario de SharedPreferences
        SharedPreferences sp = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);
        if (idUsuario == -1) {
            Toast.makeText(this, "Error: Sesión no válida", Toast.LENGTH_SHORT).show();
            return;
        }
        favoritoViewModel.listarFavoritos(idUsuario).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                adapter.setLista(response.getData());
            } else {
                String msg = (response != null) ? response.getMessage() : "Error al cargar favoritos";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarMisFavoritos();
    }
}
