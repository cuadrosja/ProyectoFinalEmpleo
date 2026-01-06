package com.example.proyectofinalempleo.presentacion.habilidad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Habilidad;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;
import java.util.List;

public class HabilidadAdapter extends RecyclerView.Adapter<HabilidadAdapter.HabilidadViewHolder> {

    private List<Habilidad> listaHabilidades = new ArrayList<>();
    private final OnHabilidadClickListener listener;

    // Interface para el clic en la "X" del chip
    public interface OnHabilidadClickListener {
        void onDeleteClick(Habilidad habilidad);
    }

    public HabilidadAdapter(OnHabilidadClickListener listener) {
        this.listener = listener;
    }

    public void setHabilidades(List<Habilidad> habilidades) {
        this.listaHabilidades = habilidades;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabilidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla tu XML del chip (asegúrate que el nombre del archivo sea item_habilidad.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habilidad, parent, false);
        return new HabilidadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabilidadViewHolder holder, int position) {
        Habilidad habilidad = listaHabilidades.get(position);
        holder.bind(habilidad, listener);
    }

    @Override
    public int getItemCount() {
        return listaHabilidades.size();
    }

    static class HabilidadViewHolder extends RecyclerView.ViewHolder {
        Chip chipHabilidad;

        public HabilidadViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencia al ID que me acabas de mostrar
            chipHabilidad = itemView.findViewById(R.id.chip_Habilidad_Item);
        }

        public void bind(final Habilidad habilidad, final OnHabilidadClickListener listener) {
            chipHabilidad.setText(habilidad.getNombreHabilidad());

            // Acción cuando el usuario toca el icono de cerrar (la X)
            chipHabilidad.setOnCloseIconClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(habilidad);
                }
            });
        }
    }
}