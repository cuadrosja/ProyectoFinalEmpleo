package com.example.proyectofinalempleo.presentacion.experiencia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.ExperienciaLaboral;

import java.util.List;

public class ListaExperienciaLaboral extends RecyclerView.Adapter<ListaExperienciaLaboral.ViewHolder> {

    private List<ExperienciaLaboral> lista;
    private OnItemClickListener listener;

    // Interfaz para gestionar las acciones de editar y eliminar
    public interface OnItemClickListener {
        void onEditClick(ExperienciaLaboral exp);
        void onDeleteClick(int id);
    }

    // Constructor que recibe la lista y el listener
    public ListaExperienciaLaboral(List<ExperienciaLaboral> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    // Método para actualizar la lista cuando los datos cambien
    public void setExperiencia(List<ExperienciaLaboral> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño de la tarjeta item_experiencia.xml
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_experiencia, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExperienciaLaboral exp = lista.get(position);

        // Seteamos los datos de texto
        holder.txtPuesto.setText(exp.getPuestoOcupado());
        holder.txtEmpresa.setText(exp.getNombreEmpresa());
        holder.txtDescripcion.setText(exp.getDescripcionTareas());

        // Formato básico de fecha
        String periodo = exp.getFechaInicio() + " - " + (exp.getFechaFin() != null ? exp.getFechaFin() : "Actualidad");
        holder.txtFecha.setText(periodo);

        // Acción para el botón Editar
        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(exp);
            }
        });

        // Acción para el botón Eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(exp.getIdExperiencia());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    // Clase interna ViewHolder para conectar con el XML
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPuesto, txtEmpresa, txtDescripcion, txtFecha;
        View btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPuesto = itemView.findViewById(R.id.txt_puesto_exp);
            txtEmpresa = itemView.findViewById(R.id.txt_empresa_exp);
            txtDescripcion = itemView.findViewById(R.id.txt_descripcion_exp);
            txtFecha = itemView.findViewById(R.id.txt_fechas_exp);

            // Asegúrate de que estos IDs existan en tu archivo item_experiencia.xml
            btnEditar = itemView.findViewById(R.id.btn_editar_exp);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar_exp);
        }
    }
}