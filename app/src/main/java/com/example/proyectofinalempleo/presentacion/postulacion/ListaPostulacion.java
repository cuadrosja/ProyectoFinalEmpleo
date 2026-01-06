package com.example.proyectofinalempleo.presentacion.postulacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Postulacion;
import java.util.List;

public class ListaPostulacion extends RecyclerView.Adapter<ListaPostulacion.ViewHolder> {

    private List<Postulacion> lista;
    // --- CAMBIO 1: Declarar la interfaz para el clic ---
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Postulacion postulacion);
    }

    // --- CAMBIO 2: Actualizar el constructor para recibir el listener ---
    public ListaPostulacion(List<Postulacion> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postulacion, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Postulacion pos = lista.get(position);

        if (pos.getEmpleo() != null) {
            holder.txtTitulo.setText(pos.getEmpleo().getTituloEmpleo());
            holder.txtEmpresa.setText(pos.getEmpleo().getEmpresa().getNombreEmpresa());
        }

        holder.txtFecha.setText("Fecha: " + pos.getFechaPostulacion());
        holder.txtEstado.setText(pos.getEstado());

        // --- CAMBIO 3: Configurar el clic en la vista ---
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (lista != null) ? lista.size() : 0;
    }

    public void setPostulaciones(List<Postulacion> nuevasPostulaciones) {
        this.lista = nuevasPostulaciones;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtEmpresa, txtFecha, txtEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_postulacion);
            txtEmpresa = itemView.findViewById(R.id.txt_empresa_postulacion);
            txtFecha = itemView.findViewById(R.id.txt_fecha_postulacion);
            txtEstado = itemView.findViewById(R.id.txt_estado_postulacion);
        }
    }
}