package com.example.proyectofinalempleo.presentacion.estudio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Estudio;
import java.util.ArrayList;
import java.util.List;

public class EstudioAdapter extends RecyclerView.Adapter<EstudioAdapter.EstudioViewHolder> {

    private List<Estudio> listaEstudios = new ArrayList<>();
    private final OnEstudioClickListener listener;

    public interface OnEstudioClickListener {
        void onEditClick(Estudio estudio);
        void onDeleteClick(int idEstudio);
    }

    public EstudioAdapter(OnEstudioClickListener listener) {
        this.listener = listener;
    }

    public void setEstudios(List<Estudio> estudios) {
        this.listaEstudios = estudios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EstudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Asegúrate de que el archivo XML en layout se llame item_estudio
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estudio, parent, false);
        return new EstudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstudioViewHolder holder, int position) {
        Estudio estudio = listaEstudios.get(position);
        holder.bind(estudio, listener);
    }

    @Override
    public int getItemCount() {
        return listaEstudios.size();
    }

    static class EstudioViewHolder extends RecyclerView.ViewHolder {
        TextView tvGradoTituloObtenido, tvInstitucion, tvFechas, tvEstado;
        ImageButton btnEditar, btnEliminar;
        public EstudioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGradoTituloObtenido = itemView.findViewById(R.id.txt_grado_estudio);
            tvInstitucion = itemView.findViewById(R.id.txt_institucion_estudio);
            tvFechas = itemView.findViewById(R.id.txt_fechas_estudio);
            tvEstado = itemView.findViewById(R.id.txt_estado_estudio);
            btnEditar = itemView.findViewById(R.id.btn_editar_estudio);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar_estudio);
        }

        public void bind(Estudio estudio, OnEstudioClickListener listener) {
            tvGradoTituloObtenido.setText(estudio.getTituloObtenido());
            tvInstitucion.setText(estudio.getInstitucion());
            String periodo = estudio.getFechaInicio() + " - " +
                    (estudio.getFechaFin() != null ? estudio.getFechaFin() : "Actualidad");

            tvFechas.setText(periodo);
            tvEstado.setText(estudio.getEstado());
            btnEditar.setOnClickListener(v -> listener.onEditClick(estudio));
            btnEliminar.setOnClickListener(v -> listener.onDeleteClick(estudio.getIdEstudio()));
        }
    }
}