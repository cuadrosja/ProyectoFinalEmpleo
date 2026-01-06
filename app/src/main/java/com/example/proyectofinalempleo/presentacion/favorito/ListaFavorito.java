package com.example.proyectofinalempleo.presentacion.favorito;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Favorito;
import java.util.List;

public class ListaFavorito extends RecyclerView.Adapter<ListaFavorito.ViewHolder> {

    private List<Favorito> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Favorito fav);
    }

    public ListaFavorito(List<Favorito> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void setLista(List<Favorito> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorito favorito = lista.get(position);

        if (favorito.getEmpleo() != null) {
            holder.txtTitulo.setText(favorito.getEmpleo().getTituloEmpleo());
            holder.txtEmpresa.setText(favorito.getEmpleo().getEmpresa().getNombreEmpresa());
        }
        // --- CONFIGURACIÓN DE CLICS ---
        //Opcion 1 : Se abre solo al presionar el botón Ver Detalles
        holder.btnVerDetalle.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(favorito);
            }
        });
        //Opcion 2 : Abrir detalle al tocar cualquier parte de la tarjeta (CardView)
        /*holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(favorito);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public void setFavorito(List<Favorito> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtEmpresa;
        Button btnVerDetalle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloFavorito);
            txtEmpresa = itemView.findViewById(R.id.txtEmpresaFavorito);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalleFavorito);
        }
    }
}