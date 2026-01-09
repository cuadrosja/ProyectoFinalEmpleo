package com.example.proyectofinalempleo.presentacion.detalle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.request.FavoritoRequest;
import com.example.proyectofinalempleo.data.request.PostulacionRequest;
import com.example.proyectofinalempleo.presentacion.Perfil.PerfilActivity;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.favorito.FavoritoViewModel;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;
import com.example.proyectofinalempleo.presentacion.postulacion.PostulacionViewModel;

import android.view.View;
import android.widget.TextView;

public class DetalleActivity extends AppCompatActivity {
    private TextView tvTitulo, tvDetalle;
    private int idEmpleo;
    private int idPostulacion;
    private int idFavorito;
    private PostulacionViewModel postulacionViewModel;
    private FavoritoViewModel favoritoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarComponentesYDatos();
        configurarMenuNavegacion();
    }
    private void configurarComponentesYDatos() {
        postulacionViewModel = new ViewModelProvider(this).get(PostulacionViewModel.class);
        favoritoViewModel = new ViewModelProvider(this).get(FavoritoViewModel.class);

        tvTitulo = findViewById(R.id.edit_titulo);
        tvDetalle = findViewById(R.id.edit_detalleEmpleo);

        Button btnPostular = findViewById(R.id.btnPostular);
        ImageButton btnFavorito = findViewById(R.id.btnFavorito);
        Button btnEliminar = findViewById(R.id.btnCancelarPostulacion);
        Button btnRetrocederPostulacion = findViewById(R.id.btnRetrocederAmisPostulaciones);
        Button btnRetrocederFavorito = findViewById(R.id.btnRetrocederAmisFavoritos);
        Button btnCancelarGuardado = findViewById(R.id.btnCancelarGuardado);


        // 2. Extraemos y mostramos los datos inmediatamente
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPostulacion = extras.getInt("ID_POSTULACION", -1);
            idEmpleo = extras.getInt("ID_EMPLEO", -1);
            idFavorito = extras.getInt("ID_FAVORITO", -1);

            String titulo = extras.getString("TITULO");
            String empresa = extras.getString("EMPRESA");
            String descripcion = extras.getString("DESCRIPCION");
            String modalidad = extras.getString("MODALIDAD", "No especificada");
            String categoria = extras.getString("CATEGORIA", "General");
            String direccion = extras.getString("DIRECCION", "No disponible");
            String fecha = extras.getString("FECHA", "");

            String origen = extras.getString("ORIGEN");

            // --- LÓGICA DE VISIBILIDAD ---
            if (origen != null && origen.equals("MIS_POSTULACIONES")) {
                // Mis Postulaciones: mostramos eliminar y retroceder, ocultamos postular y favorito
                if (btnEliminar != null) btnEliminar.setVisibility(View.VISIBLE);
                if (btnRetrocederPostulacion != null) btnRetrocederPostulacion.setVisibility(View.VISIBLE);
                if (btnPostular != null) btnPostular.setVisibility(View.GONE);
                if (btnFavorito != null) btnFavorito.setVisibility(View.GONE);
                if (btnCancelarGuardado != null) btnCancelarGuardado.setVisibility(View.GONE);
                if (btnRetrocederFavorito != null) btnRetrocederFavorito.setVisibility(View.GONE);
            } else if (origen != null && origen.equals("MIS_FAVORITOS")) {
                // Mis Favoritos: mostramos Postular y Quitar Favorito
                if (btnPostular != null) btnPostular.setVisibility(View.VISIBLE);
                if (btnCancelarGuardado != null) btnCancelarGuardado.setVisibility(View.VISIBLE);
                if (btnRetrocederFavorito != null) btnRetrocederFavorito.setVisibility(View.VISIBLE);
                if (btnFavorito != null) btnFavorito.setVisibility(View.GONE);
                if (btnEliminar != null) btnEliminar.setVisibility(View.GONE);
                if (btnRetrocederPostulacion != null) btnRetrocederPostulacion.setVisibility(View.GONE);
            } else {
                // Lista de Empleos Detalle
                if (btnPostular != null) btnPostular.setVisibility(View.VISIBLE);
                if (btnFavorito != null) btnFavorito.setVisibility(View.VISIBLE);
                if (btnCancelarGuardado != null) btnCancelarGuardado.setVisibility(View.GONE);
                if (btnEliminar != null) btnEliminar.setVisibility(View.GONE);
                if (btnRetrocederPostulacion != null) btnRetrocederPostulacion.setVisibility(View.GONE);
                if (btnRetrocederFavorito != null) btnRetrocederFavorito.setVisibility(View.GONE);
            }

            tvTitulo.setText(titulo);

            String colorNegro = "#000000";

            String contenidoHtml =
                    "<font color='" + colorNegro + "'><b>🏢 Empresa:</b> " + empresa + "</font><br>" +
                            "<font color='" + colorNegro + "'><b>📍 Ubicación:</b> " + direccion + "</font><br>" +
                            "<font color='" + colorNegro + "'><b>📂 Categoría:</b> " + categoria + "</font><br>" +
                            "<font color='" + colorNegro + "'><b>💻 Modalidad:</b> " + modalidad + "</font><br>" +
                            "<hr>" +
                            "<b>Descripción del Puesto:</b><br>" + descripcion + "<br><br>" +
                            "<font color='" + colorNegro + "'><b>📅 Publicado el: </b><b>" + (fecha.length() > 10 ? fecha.substring(0, 10) : fecha) + "</b></font>";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvDetalle.setText(android.text.Html.fromHtml(contenidoHtml, android.text.Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvDetalle.setText(android.text.Html.fromHtml(contenidoHtml));
            }
        }
    }
    public void onClickPostular(View v) {
        SharedPreferences sp = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario == -1 || idEmpleo == -1) {
            Toast.makeText(this, "Error: Datos de sesión o empleo no encontrados", Toast.LENGTH_LONG).show();
            return;
        }
        PostulacionRequest request = new PostulacionRequest(idUsuario, idEmpleo);
        postulacionViewModel.registrarPostulacion(request).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "¡Postulación exitosa!", Toast.LENGTH_SHORT).show();
                finish(); // Cerramos y volvemos a la lista
            } else {
                String error = (response != null) ? response.getMessage() : "Error al postular";
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onClickFavorito(View v) {
        SharedPreferences sp = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario == -1 || idEmpleo == -1) {
            Toast.makeText(this, "Debe iniciar sesión para guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        FavoritoRequest request = new FavoritoRequest(idUsuario, idEmpleo);
        // Llamamos al ViewModel de favoritos
        favoritoViewModel.registrarFavorito(request).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "⭐ Añadido a favoritos", Toast.LENGTH_SHORT).show();
                // Opcional: Cambiar el icono del botón aquí
                ImageButton btn = (ImageButton) v;
                // CAMBIO: Además de setear la imagen, le ponemos un tinte rojo
                btn.setImageResource(R.drawable.icon_favorito);
                btn.setColorFilter(android.graphics.Color.RED);
            } else {
                String msg = (response != null) ? response.getMessage() : "Ya está en favoritos";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void configurarMenuNavegacion() {
        Button btnNavInicio = findViewById(R.id.btnNavInicio);
        Button btnNavEmpleo = findViewById(R.id.btnNavEmpleo);
        Button btnNavPerfil = findViewById(R.id.btnNavPerfil);

        if (btnNavInicio != null) btnNavInicio.setOnClickListener(this::navegarMenu);
        if (btnNavEmpleo != null) btnNavEmpleo.setOnClickListener(this::navegarMenu);
        if (btnNavPerfil != null) btnNavPerfil.setOnClickListener(this::navegarMenu);
    }
    public void onClickEliminarPostulacion(View v) {
        // Validamos que tengamos el ID único de la postulación
        if (idPostulacion == -1) {
            Toast.makeText(this, "Error: No se encontró la postulación", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamamos al ViewModel usando SOLO el ID de la postulación
        postulacionViewModel.cancelarPostulacion(idPostulacion).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Postulación eliminada", Toast.LENGTH_SHORT).show();
                finish(); // Cierra y activa el onResume de la lista
            } else {
                String msg = (response != null) ? response.getMessage() : "Error desconocido";
                Toast.makeText(this, "No se pudo eliminar: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onClickEliminarFavorito(View v) {
        if (idFavorito == -1) {
            Toast.makeText(this, "Error: No se encontró el identificador del favorito", Toast.LENGTH_SHORT).show();
            return;
        }
        favoritoViewModel.eliminarFavorito(idFavorito).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Favorito eliminado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String msg = (response != null) ? response.getMessage() : "Error al eliminar";
                Toast.makeText(this, "No se pudo eliminar: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onClickRetrocederPostulaciones(View v) {
        startActivity(new Intent(this, com.example.proyectofinalempleo.presentacion.postulacion.PostulacionActivity.class));
        finish();
    }
    public void onClickRetrocederFavoritos(View v) {
        // Simplemente cerramos la actividad para volver a FavoritoActivity
        finish();
    }
    public void navegarMenu(View v) {
        int id = v.getId();
        Intent intent = null;

        if (id == R.id.btnNavInicio) {
            intent = new Intent(this, InicioActivity.class);
        } else if (id == R.id.btnNavEmpleo) {
            intent = new Intent(this, EmpleoActivity.class);
            // TRUCO PARA VELOCIDAD: Si la lista ya existe, solo tráela al frente
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        } else if (id == R.id.btnNavPerfil) {
            intent = new Intent(this, PerfilActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0, 0); // Elimina animaciones pesadas
            finish(); // Cerramos el detalle para liberar memoria
        }
    }
}