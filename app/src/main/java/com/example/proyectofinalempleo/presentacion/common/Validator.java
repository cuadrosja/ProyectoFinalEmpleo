package com.example.proyectofinalempleo.presentacion.common;

import android.util.Patterns;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase de utilidad para validar campos EditText de forma encadenada.
 * Ejemplo: Validator.with(etEmail).required().email().validate();
 */
public class Validator {

    private final EditText editText;
    private String errorMessage;

    private Validator(EditText editText) {
        this.editText = editText;
        // Limpia el error visual previo cada vez que se inicia una validación
        this.editText.setError(null);
    }

    public static Validator with(EditText editText) {
        return new Validator(editText);
    }

    // Campo obligatorio
    public Validator required() {
        String value = editText.getText().toString().trim();
        if (errorMessage == null && value.isEmpty()) {
            errorMessage = "Este campo es obligatorio";
        }
        return this;
    }

    // Validación de Email (¡Aquí está!)
    public Validator email() {
        String value = editText.getText().toString().trim();
        if (errorMessage == null && !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Formato de email incorrecto";
        }
        return this;
    }

    // Longitud exacta (para DNI, Celular, etc.)
    public Validator length(int len) {
        String value = editText.getText().toString().trim();
        if (errorMessage == null && value.length() != len) {
            errorMessage = "Debe tener exactamente " + len + " caracteres";
        }
        return this;
    }

    // Longitud mínima (para nombres, puestos, contraseñas)
    public Validator minLength(int min) {
        String value = editText.getText().toString().trim();
        if (errorMessage == null && value.length() < min) {
            errorMessage = "Debe tener al menos " + min + " caracteres";
        }
        return this;
    }

    // Validación de fecha en formato Día/Mes/Año
    public Validator isDate() {
        String value = editText.getText().toString().trim();
        if (errorMessage == null && !isValidDate(value)) {
            errorMessage = "Fecha inválida (dd/MM/yyyy)";
        }
        return this;
    }

    /**
     * Aplica el error visual en el EditText si alguna regla falló.
     * @return true si el campo es válido, false si hay error.
     */
    public boolean validate() {
        if (errorMessage != null) {
            editText.setError(errorMessage);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) return false;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // No permite fechas lógicas imposibles (ej. 31/02)

        try {
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}