package com.example.proyectofinalempleo.presentacion.Perfil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.request.RegistroRequest;
import com.example.proyectofinalempleo.presentacion.cv.CvActivity;
import com.example.proyectofinalempleo.presentacion.empleo.EmpleoActivity;
import com.example.proyectofinalempleo.presentacion.favorito.FavoritoActivity;
import com.example.proyectofinalempleo.presentacion.inicio.InicioActivity;
import com.example.proyectofinalempleo.presentacion.postulacion.PostulacionActivity;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.widget.PopupMenu;

import com.example.proyectofinalempleo.presentacion.registroLogin.LoginViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

public class PerfilActivity extends AppCompatActivity {
    private TextView txtNombre, txtEmail, txtTelefono, txtCartaPresentacion;
    private MaterialCardView cardCarta;
    private ImageView imgFotoPerfil, btnEditar, btnCambiarFoto;
    private LoginViewModel loginViewModel;
    private String base64Imagen = "";
    private static final String NOMBRE_ARCHIVO = "AUTH_PREFS";

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        // 1. Mostrar la foto en el círculo de una vez
                        Glide.with(this).load(uri).circleCrop().into(imgFotoPerfil);

                        // 2. Convertir a Base64
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                        String imagenParaSubir = "data:image/jpeg;base64," + Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);

                        // 3. SE SUBE SOLA AQUÍ (Método independiente)
                        actualizarSoloFoto(imagenParaSubir);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginViewModel = new androidx.lifecycle.ViewModelProvider(this).get(LoginViewModel.class);

        inicializarYMostrarDatos();
        configurarMenuNavegacion();
        configurarEventos();
    }
    private void actualizarSoloFoto(String imagenBase64) {
        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            RegistroRequest request = new RegistroRequest();
            // Cargamos los datos actuales para no enviarlos vacíos
            request.setNombre(sp.getString("nombre", ""));
            request.setApellidos(sp.getString("apellidos", ""));
            request.setEmail(sp.getString("email", ""));
            request.setTelefono(sp.getString("telefono", ""));
            request.setFotoPerfil(imagenBase64);

            request.setCartaPresentacion(sp.getString("carta_presentacion", ""));

            loginViewModel.actualizarUsuario(idUsuario, request).observe(this, response -> {
                if (response != null && response.isSuccess()) {
                    // 1. Guardar la foto localmente para que no se borre al cerrar
                    sp.edit().putString("fotoPerfil", imagenBase64).apply();

                    // 2. Actualizar el circulito pequeño del menú
                    ShapeableImageView btnOpciones = findViewById(R.id.OpcionesMenu);
                    if (btnOpciones != null) {
                        Glide.with(this).load(imagenBase64).circleCrop().into(btnOpciones);
                    }
                    Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al subir la foto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void inicializarYMostrarDatos() {
        // Vincular
        txtNombre = findViewById(R.id.txtNombreCompleto);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefono = findViewById(R.id.txtTelefono);
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        btnEditar = findViewById(R.id.btnEditarDatos);
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto);
        txtCartaPresentacion = findViewById(R.id.txtCartaPresentacion);
        cardCarta = findViewById(R.id.cardCartaPresentacion);

        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        String nombre = sp.getString("nombre", "Usuario");
        String apellidos = sp.getString("apellidos", "");
        txtNombre.setText(nombre + " " + apellidos);
        txtEmail.setText(sp.getString("email", ""));
        txtTelefono.setText(sp.getString("telefono", ""));
        String urlFoto = sp.getString("fotoPerfil", "");

        if (idUsuario != -1) {
            // Mostramos un log para saber que entramos aquí
            System.out.println("DEBUG: Solicitando perfil para ID: " + idUsuario);

            loginViewModel.obtenerUsuarioPorIdUsuario(idUsuario).observe(this, response -> {
                if (response != null && response.isSuccess() && response.getData() != null) {
                    String cartaServidor = response.getData().getCartaPresentacion();

                    if (cartaServidor != null && !cartaServidor.trim().isEmpty()) {
                        // 1. PRIMERO actualizamos la vista para que el usuario vea el cambio
                        txtCartaPresentacion.setText(cartaServidor);
                        cardCarta.setVisibility(View.VISIBLE);

                        // 2. DESPUÉS guardamos en caché
                        sp.edit().putString("carta_presentacion", cartaServidor).apply();
                        System.out.println("DEBUG: Carta cargada: " + cartaServidor);
                    } else {
                        cardCarta.setVisibility(View.GONE);
                    }
                } else {
                    // Si falla, ocultamos el card para que no se vea el "Cargando..."
                    cardCarta.setVisibility(View.GONE);
                    System.out.println("DEBUG: La respuesta falló o vino vacía");
                }
            });
        }

        if (imgFotoPerfil != null) {
            Glide.with(this)
                    .load(urlFoto)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgFotoPerfil);
        }
        ShapeableImageView btnOpciones = findViewById(R.id.OpcionesMenu);
        if (btnOpciones != null) {
            Glide.with(this)
                    .load(urlFoto)
                    .circleCrop()
                    .into(btnOpciones);
        }
    }
    private void configurarMenuNavegacion() {
        // 1. Buscamos los botones del menú inferior
        Button btnNavInicio = findViewById(R.id.btnNavInicio);
        Button btnNavEmpleo = findViewById(R.id.btnNavEmpleo);
        Button btnNavPerfil = findViewById(R.id.btnNavPerfil);

        // 2. Activamos los clics usando el método navegarMenu
        if (btnNavInicio != null) btnNavInicio.setOnClickListener(this::navegarMenu);
        if (btnNavEmpleo != null) btnNavEmpleo.setOnClickListener(this::navegarMenu);
        if (btnNavPerfil != null) btnNavPerfil.setOnClickListener(this::navegarMenu);
        if (btnNavPerfil != null) btnNavPerfil.setSelected(true);
    }
    public void navegarMenu(View v) {
        int id = v.getId();
        if (id == R.id.btnNavPerfil) return;

        Intent intent = null;
        if (id == R.id.btnNavInicio) intent = new Intent(this, InicioActivity.class);
        else if (id == R.id.btnNavEmpleo) intent = new Intent(this, EmpleoActivity.class);

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    private void configurarEventos() {
        if (btnCambiarFoto != null) {
            btnCambiarFoto.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            });
        }

        ShapeableImageView btnOpciones = findViewById(R.id.OpcionesMenu);
        if (btnOpciones != null) {
            btnOpciones.setOnClickListener(v -> mostrarMenuOpciones(v));
        }
        if (btnEditar != null) {
            btnEditar.setOnClickListener(v -> mostrarDialogoEditar());
        }
    }
    private void mostrarMenuOpciones(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.menu_perfil, popup.getMenu());

        try {
            java.lang.reflect.Field field = popup.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuHelper = field.get(popup);
            Class<?> classPopupHelper = Class.forName(menuHelper.getClass().getName());
            java.lang.reflect.Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceIcons.invoke(menuHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        popup.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menu_cv) {
                Intent intent = new Intent(this, CvActivity.class);
                startActivity(intent);
                return true;
            }
            if (id == R.id.menu_favoritos) {
                Intent intent = new Intent(this, FavoritoActivity.class);
                startActivity(intent);
                return true;
            }
            if (id == R.id.menu_postulaciones) {
                startActivity(new Intent(this, PostulacionActivity.class));
                return true;
            }
            if (id == R.id.menu_cerrar_sesion) {
                confirmarCierreSesion();
                return true;
            }
            return false;
        });
        popup.show();
    }
    private void confirmarCierreSesion() {
        SharedPreferences sharedPref = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();

        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, InicioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void mostrarDialogoEditar() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int margin = (int) (20 * getResources().getDisplayMetrics().density);
        container.setPadding(margin, margin, margin, 0);

        SharedPreferences sp = getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);

        // --- SECCIÓN NOMBRE ---
        TextView lblNombre = new TextView(this);
        lblNombre.setText("Nombre");
        lblNombre.setTextSize(14);
        lblNombre.setPadding(0, 10, 0, 5);
        container.addView(lblNombre);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Ingrese su Nombre");
        etNombre.setText(sp.getString("nombre", ""));
        container.addView(etNombre);

        // --- SECCIÓN APELLIDOS ---
        TextView lblApellido = new TextView(this);
        lblApellido.setText("Apellidos");
        lblApellido.setTextSize(14);
        lblApellido.setPadding(0, 20, 0, 5);
        container.addView(lblApellido);

        final EditText etApellido = new EditText(this);
        etApellido.setHint("Ingrese su Apellido");
        etApellido.setText(sp.getString("apellidos", ""));
        container.addView(etApellido);

        // --- SECCIÓN CORREO ---
        TextView lblEmail = new TextView(this);
        lblEmail.setText("Correo Electrónico");
        lblEmail.setTextSize(14);
        lblEmail.setPadding(0, 20, 0, 5);
        container.addView(lblEmail);

        final EditText etEmail = new EditText(this);
        etEmail.setHint("usuario@correo.com");
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmail.setText(sp.getString("email", ""));
        container.addView(etEmail);

        // --- SECCIÓN TELÉFONO ---
        TextView lblTelefono = new TextView(this);
        lblTelefono.setText("Teléfono");
        lblTelefono.setTextSize(14);
        lblTelefono.setPadding(0, 20, 0, 5);
        container.addView(lblTelefono);

        final EditText etTelefono = new EditText(this);
        etTelefono.setHint("Ejemplo: 999 888 777");
        etTelefono.setInputType(InputType.TYPE_CLASS_PHONE);
        etTelefono.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        etTelefono.setText(sp.getString("telefono", ""));
        container.addView(etTelefono);
        ////////////////////////////////////////////////////////////////////////////////////////////
        new AlertDialog.Builder(this)
                .setTitle("Actualizar Perfil")
                .setView(container)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Llamamos con los 4 parámetros
                    ejecutarActualizacion(
                            etNombre.getText().toString(),
                            etApellido.getText().toString(),
                            etEmail.getText().toString(),
                            etTelefono.getText().toString()
                    );
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void ejecutarActualizacion(String nom, String ape, String mail, String telf) {
        SharedPreferences sp = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE);
        int idUsuario = sp.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            RegistroRequest request = new RegistroRequest();
            request.setNombre(nom);
            request.setApellidos(ape);
            request.setEmail(mail);
            request.setTelefono(telf);

            request.setCartaPresentacion(txtCartaPresentacion.getText().toString());

            loginViewModel.actualizarUsuario(idUsuario, request).observe(this, response -> {
                if (response != null && response.isSuccess()) {
                    sp.edit()
                            .putString("nombre", nom)
                            .putString("apellidos", ape)
                            .putString("email", mail)
                            .putString("telefono", telf)
                            .apply();

                    txtNombre.setText(nom + " " + ape);
                    txtEmail.setText(mail);
                    txtTelefono.setText(telf);
                    base64Imagen = "";

                    Toast.makeText(this, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    // 3. CAMBIO IMPORTANTE: Vamos a ver qué error dice el servidor
                    String errorServidor = (response != null) ? response.getMessage() : "Respuesta nula";
                    Toast.makeText(this, "Error del servidor: " + errorServidor, Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(this, "Error: No se encontró el ID del usuario", Toast.LENGTH_SHORT).show();
        }
    }
}