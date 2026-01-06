package com.example.proyectofinalempleo.presentacion.idioma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Idioma;
import java.util.ArrayList;
import java.util.List;

public class IdiomaAdapter extends RecyclerView.Adapter<IdiomaAdapter.IdiomaViewHolder> {

    private List<Idioma> listaIdiomas = new ArrayList<>();
    private final OnIdiomaClickListener listener;

    public interface OnIdiomaClickListener {
        void onEditClick(Idioma idioma);
        void onDeleteClick(int idIdioma);
    }

    public IdiomaAdapter(IdiomaAdapter.OnIdiomaClickListener listener) {
        this.listener = listener;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.listaIdiomas = idiomas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IdiomaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Asegúrate de crear el archivo layout/item_idioma.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_idioma, parent, false);
        return new IdiomaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IdiomaViewHolder holder, int position) {
        Idioma idioma = listaIdiomas.get(position);
        holder.bind(idioma, listener);
    }

    @Override
    public int getItemCount() {
        return listaIdiomas.size();
    }

    static class IdiomaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreIdioma, tvNivel;
        ImageButton btnEditar, btnEliminar;

        public IdiomaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencias a los IDs de item_idioma.xml
            tvNombreIdioma = itemView.findViewById(R.id.txt_nombre_idioma_item);
            tvNivel = itemView.findViewById(R.id.txt_nivel_idioma_item);
            btnEditar = itemView.findViewById(R.id.btn_editar_idioma);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar_idioma);
        }

        public void bind(Idioma idioma, OnIdiomaClickListener listener) {
            tvNombreIdioma.setText(idioma.getNombreIdioma());
            tvNivel.setText("Nivel: " + idioma.getNivel());

            btnEditar.setOnClickListener(v -> listener.onEditClick(idioma));
            btnEliminar.setOnClickListener(v -> listener.onDeleteClick(idioma.getIdIdioma()));
        }
    }
}