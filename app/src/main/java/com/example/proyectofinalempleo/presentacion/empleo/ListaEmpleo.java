package com.example.proyectofinalempleo.presentacion.empleo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinalempleo.R;
import com.example.proyectofinalempleo.data.model.Empleo;
import java.util.List;

// Heredamos de RecyclerView.Adapter para que Android sepa que este es el "traductor"
public class ListaEmpleo extends RecyclerView.Adapter<ListaEmpleo.ViewHolder> {

    private List<Empleo> lista;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Empleo empleo);
    }
    public ListaEmpleo(List<Empleo> lista,OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleo, parent, false);
        return new ViewHolder(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder contenedor, int posicion) {
        Empleo empleoSeleccionado = lista.get(posicion);
        contenedor.txtTitulo.setText(empleoSeleccionado.getTituloEmpleo());
        contenedor.txtEmpresa.setText(empleoSeleccionado.getEmpresa().getNombreEmpresa() + " • " + empleoSeleccionado.getModalidad());
        contenedor.txtUbicacion.setText(empleoSeleccionado.getEmpresa().getDireccion());

        if (empleoSeleccionado.getEmpresa() != null && empleoSeleccionado.getEmpresa().getLogoUrl() != null) {
            com.bumptech.glide.Glide.with(contenedor.itemView.getContext())
                    .load(empleoSeleccionado.getEmpresa().getLogoUrl())
                    .placeholder(R.drawable.empleo_ya)
                    .error(R.drawable.empleo_ya)
                    //.circleCrop()
                    .into(contenedor.imgLogo);
        }else{
            com.bumptech.glide.Glide.with(contenedor.itemView.getContext())
                    .load(R.drawable.empleo_ya)
                    .into(contenedor.imgLogo);
        }
        contenedor.itemView.setOnClickListener(v -> {
            int posicionReal = contenedor.getAdapterPosition();
            if (listener != null && posicionReal != RecyclerView.NO_POSITION) {
                listener.onItemClick(lista.get(posicionReal));
            }
        });
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtEmpresa, txtUbicacion;
        ImageView imgLogo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_empleo);
            txtEmpresa = itemView.findViewById(R.id.text_empresa);
            txtUbicacion = itemView.findViewById(R.id.txt_ubicacion);
            imgLogo = itemView.findViewById(R.id.img_empleo);
        }
    }
    public void filtrar(List<Empleo> listaFiltrada) {
        this.lista = listaFiltrada;
        notifyDataSetChanged();
    }
    public void setEmpleos(List<Empleo> nuevosEmpleos) {
        this.lista = nuevosEmpleos;
        notifyDataSetChanged();
    }
}