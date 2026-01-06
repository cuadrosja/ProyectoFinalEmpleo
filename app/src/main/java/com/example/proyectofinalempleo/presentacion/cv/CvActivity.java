package com.example.proyectofinalempleo.presentacion.cv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Estudio;
import com.example.proyectofinalempleo.data.model.ExperienciaLaboral;
import com.example.proyectofinalempleo.data.model.Habilidad;
import com.example.proyectofinalempleo.data.model.Idioma;
import com.example.proyectofinalempleo.data.request.RegistroRequest;
import com.example.proyectofinalempleo.presentacion.estudio.EstudioActivity;
import com.example.proyectofinalempleo.presentacion.estudio.EstudioViewModel;
import com.example.proyectofinalempleo.presentacion.experiencia.ExperienciaLaboralActivity;
import com.example.proyectofinalempleo.presentacion.experiencia.ExperienciaLaboralViewModel;
import com.example.proyectofinalempleo.presentacion.habilidad.HabilidadActivity;
import com.example.proyectofinalempleo.presentacion.habilidad.HabilidadViewModel;
import com.example.proyectofinalempleo.presentacion.idioma.IdiomaActivity;
import com.example.proyectofinalempleo.presentacion.idioma.IdiomaViewModel;
import com.example.proyectofinalempleo.presentacion.registroLogin.LoginViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CvActivity extends AppCompatActivity {

    private TextView txtPresentacion;
    private ShapeableImageView btnActualizarPresentacion;
    private LinearLayout contenedorEstudios, contenedorExperiencia, contenedorIdiomas;
    private ChipGroup cgHabilidades;
    private View btnVerListaExp, btnVerListaEstudios, btnVerListaIdiomas, btnVerListaHabilidades;
    private EstudioViewModel estudioViewModel;
    private ExperienciaLaboralViewModel experienciaViewModel;
    private IdiomaViewModel idiomaViewModel;
    private HabilidadViewModel habilidadViewModel;
    private LoginViewModel loginViewModel;

    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cv);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Descomentar si el scroll con mouse falla en dispositivos físicos
        /*NestedScrollView nestedScrollView = findViewById(R.id.id_scroll);
        if (nestedScrollView != null) {
            // Esto habilita que la rueda del mouse "empuje" el scroll
            nestedScrollView.setNestedScrollingEnabled(true);
            // Opcional: para que empiece desde arriba siempre
            nestedScrollView.setSmoothScrollingEnabled(true);
        }*/

        // Inicializar todos los ViewModels
        estudioViewModel = new ViewModelProvider(this).get(EstudioViewModel.class);
        experienciaViewModel = new ViewModelProvider(this).get(ExperienciaLaboralViewModel.class);
        idiomaViewModel = new ViewModelProvider(this).get(IdiomaViewModel.class);
        habilidadViewModel = new ViewModelProvider(this).get(HabilidadViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        vincularComponentes();
        configurarBotonesVerLista();
        cargarDatosCv();
    }

    private void vincularComponentes() {
        txtPresentacion = findViewById(R.id.txt_presentacion_cv);
        contenedorEstudios = findViewById(R.id.contenedor_estudios_cv);
        contenedorExperiencia = findViewById(R.id.contenedor_experiencia_cv);
        contenedorIdiomas = findViewById(R.id.contenedor_idiomas_cv);
        cgHabilidades = findViewById(R.id.cg_habilidades_cv);

        btnVerListaExp = findViewById(R.id.btn_ver_lista_exp);
        btnVerListaEstudios = findViewById(R.id.btn_ver_lista_estudios);
        btnVerListaIdiomas = findViewById(R.id.btn_ver_lista_idiomas);
        btnVerListaHabilidades = findViewById(R.id.btn_ver_lista_habilidades);
        btnActualizarPresentacion = findViewById(R.id.btn_actualizar_presentacion);
    }

    private void configurarBotonesVerLista() {
        btnVerListaExp.setOnClickListener(v -> startActivity(new Intent(this, ExperienciaLaboralActivity.class)));
        btnVerListaEstudios.setOnClickListener(v -> startActivity(new Intent(this, EstudioActivity.class)));
        btnVerListaIdiomas.setOnClickListener(v -> startActivity(new Intent(this, IdiomaActivity.class)));
        btnVerListaHabilidades.setOnClickListener(v -> startActivity(new Intent(this, HabilidadActivity.class)));

        if (btnActualizarPresentacion != null) {
            btnActualizarPresentacion.setOnClickListener(v -> mostrarDialogoCarta());
        }
    }
    private void mostrarDialogoCarta() {
        View vista = getLayoutInflater().inflate(R.layout.activity_editar_presentacion, null);
        TextInputEditText etCarta = vista.findViewById(R.id.et_nueva_carta);
        MaterialButton btnGuardar = vista.findViewById(R.id.btn_guardar_carta);

        etCarta.setText(txtPresentacion.getText().toString());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(vista).create();

        btnGuardar.setOnClickListener(v -> {
            String nuevoTexto = etCarta.getText().toString().trim();
            if (!nuevoTexto.isEmpty()) {
                actualizarCartaEnServidor(nuevoTexto, dialog);
            }
        });
        dialog.show();
    }
    private void actualizarCartaEnServidor(String nuevoTexto, AlertDialog dialog) {
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            RegistroRequest request = new RegistroRequest();
            request.setCartaPresentacion(nuevoTexto);
            loginViewModel.actualizarUsuario(idUsuario, request).observe(this, response -> {
                if (response != null && response.isSuccess()) {
                    sp.edit().putString("cartaPresentacion", nuevoTexto).apply();
                    txtPresentacion.setText(nuevoTexto);
                    Toast.makeText(this, "¡Actualizado!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Solo cerramos si la API respondió OK
                } else {
                    Toast.makeText(this, "Error al guardar, intenta de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarDatosCv() {
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);
        if (idUsuario != -1) {
            txtPresentacion.setText(sp.getString("cartaPresentacion", "Habilidad y capacidad para adaptarme..."));
            estudioViewModel.obtenerEstudios(idUsuario).observe(this, response -> {
                if (response != null && response.isSuccess()) mostrarEstudios(response.getData());
            });
            experienciaViewModel.obtenerExperiencias(idUsuario).observe(this, response -> {
                if (response != null && response.isSuccess()) mostrarExperiencias(response.getData());
            });
            idiomaViewModel.obtenerIdiomas(idUsuario).observe(this, response -> {
                if (response != null && response.isSuccess()) mostrarIdiomas(response.getData());
            });
            habilidadViewModel.obtenerHabilidades(idUsuario).observe(this, response -> {
                if (response != null && response.isSuccess()) mostrarHabilidades(response.getData());
            });
        }
    }
    private void mostrarEstudios(List<Estudio> lista) {
        if (contenedorEstudios == null) return;
        contenedorEstudios.removeAllViews();
        // Buscamos si la Lista esta Vacio Muestra Agregar Datos y la Lista hay datos Ver lista Completa
        TextView txtBotonListaEst = btnVerListaEstudios.findViewById(R.id.txt_label_boton);
        if (lista == null || lista.isEmpty()) {
            if (txtBotonListaEst != null) txtBotonListaEst.setText("Agregar Datos");
            return;
        } else {
            if (txtBotonListaEst != null) txtBotonListaEst.setText("Ver lista Completa");
        }

        int limite = Math.min(lista.size(), 2); // MUESTRA SOLO 2 REGISTROS
        for (int i = 0; i < limite; i++) {
            Estudio est = lista.get(i);
            View v = LayoutInflater.from(this).inflate(R.layout.item_estudio, contenedorEstudios, false);
            ((TextView) v.findViewById(R.id.txt_grado_estudio)).setText(est.getTituloObtenido());
            ((TextView) v.findViewById(R.id.txt_institucion_estudio)).setText(est.getInstitucion());
            v.findViewById(R.id.btn_editar_estudio).setVisibility(View.GONE);
            v.findViewById(R.id.btn_eliminar_estudio).setVisibility(View.GONE);
            contenedorEstudios.addView(v);
        }
    }
    private void mostrarExperiencias(List<ExperienciaLaboral> lista) {
        if (contenedorExperiencia == null) return;
        contenedorExperiencia.removeAllViews();
        // Buscamos si la Lista esta Vacio Muestra Agregar Datos y la Lista hay datos Ver lista Completa
        TextView txtBotonListaExp = btnVerListaExp.findViewById(R.id.txt_label_boton);
        if (lista == null || lista.isEmpty()) {
            if (txtBotonListaExp != null) txtBotonListaExp.setText("Agregar Datos");
            return;
        } else {
            if (txtBotonListaExp != null) txtBotonListaExp.setText("Ver lista Completa");
        }

        int limite = Math.min(lista.size(), 2);  // MUESTRA SOLO 2 REGISTROS
        for (int i = 0; i < limite; i++) {
            ExperienciaLaboral exp = lista.get(i);
            View v = LayoutInflater.from(this).inflate(R.layout.item_experiencia, contenedorExperiencia, false);
            ((TextView) v.findViewById(R.id.txt_puesto_exp)).setText(exp.getPuestoOcupado());
            ((TextView) v.findViewById(R.id.txt_empresa_exp)).setText(exp.getNombreEmpresa());
            String periodo = exp.getFechaInicio() + " - " + (exp.isTrabajoActual() ? "Actualidad" : exp.getFechaFin());
            ((TextView) v.findViewById(R.id.txt_fechas_exp)).setText(periodo);
            ((TextView) v.findViewById(R.id.txt_descripcion_exp)).setText(exp.getDescripcionTareas());
            v.findViewById(R.id.btn_editar_exp).setVisibility(View.GONE);
            v.findViewById(R.id.btn_eliminar_exp).setVisibility(View.GONE);
            contenedorExperiencia.addView(v);
        }
    }
    private void mostrarIdiomas(List<Idioma> lista) {
        if (contenedorIdiomas == null) return;
        contenedorIdiomas.removeAllViews();
        // Buscamos si la Lista esta Vacio Muestra Agregar Datos y la Lista hay datos Ver lista Completa
        TextView txtBotonListaIdioma = btnVerListaIdiomas.findViewById(R.id.txt_label_boton);
        if (lista == null || lista.isEmpty()) {
            if (txtBotonListaIdioma != null) txtBotonListaIdioma.setText("Agregar Datos");
            return;
        } else {
            if (txtBotonListaIdioma != null) txtBotonListaIdioma.setText("Ver lista Completa");
        }

        int limite = Math.min(lista.size(), 2);  // MUESTRA SOLO 2 REGISTROS
        for (int i = 0; i < limite; i++) {
            Idioma idio = lista.get(i);
            View v = LayoutInflater.from(this).inflate(R.layout.item_idioma, contenedorIdiomas, false);
            ((TextView) v.findViewById(R.id.txt_nombre_idioma_item)).setText(idio.getNombreIdioma());
            ((TextView) v.findViewById(R.id.txt_nivel_idioma_item)).setText("Nivel: " + idio.getNivel());
            v.findViewById(R.id.btn_editar_idioma).setVisibility(View.GONE);
            v.findViewById(R.id.btn_eliminar_idioma).setVisibility(View.GONE);
            contenedorIdiomas.addView(v);
        }
    }
    private void mostrarHabilidades(List<Habilidad> lista) {
        if (cgHabilidades == null) return;
        cgHabilidades.removeAllViews();
        // Buscamos si la Lista esta Vacio Muestra Agregar Datos y la Lista hay datos Ver lista Completa
        TextView txtBotonListaHab = btnVerListaHabilidades.findViewById(R.id.txt_label_boton);
        if (lista == null || lista.isEmpty()) {
            if (txtBotonListaHab != null) txtBotonListaHab.setText("Agregar Datos");
            return;
        } else {
            if (txtBotonListaHab != null) txtBotonListaHab.setText("Ver lista Completa");
        }

        int limite = Math.min(lista.size(), 6);  // MUESTRA SOLO 6 REGISTROS
        for (int i = 0; i < limite; i++) {
            Habilidad hab = lista.get(i);
            Chip chip = new Chip(this);
            chip.setText(hab.getNombreHabilidad());
            chip.setChipBackgroundColorResource(R.color.white);
            cgHabilidades.addView(chip);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosCv();
    }
}

