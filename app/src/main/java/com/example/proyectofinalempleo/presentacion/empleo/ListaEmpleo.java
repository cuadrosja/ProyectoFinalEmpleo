package com.example.proyectofinalempleo.presentacion.empleo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        // Usamos el diseño morado (item_empleo.xml) que hiciste antes
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleo, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder contenedor, int posicion) {
        Empleo empleoSeleccionado = lista.get(posicion);

        contenedor.txtTitulo.setText(empleoSeleccionado.getTituloEmpleo());
        contenedor.txtEmpresa.setText(empleoSeleccionado.getEmpresa().getNombreEmpresa() + " • " + empleoSeleccionado.getModalidad());
        contenedor.txtUbicacion.setText(empleoSeleccionado.getEmpresa().getDireccion());
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

    // Esta clase interna busca los componentes dentro de item_empleo.xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtEmpresa, txtUbicacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de que estos IDs coincidan con los de tu XML item_empleo
            txtTitulo = itemView.findViewById(R.id.txt_titulo_empleo);
            txtEmpresa = itemView.findViewById(R.id.text_empresa);
            txtUbicacion = itemView.findViewById(R.id.txt_ubicacion);
        }
    }
    // Este método es el que permite que el buscador actualice la lista
    public void filtrar(List<Empleo> listaFiltrada) {
        this.lista = listaFiltrada;
        notifyDataSetChanged(); // Esto refresca la pantalla con los nuevos datos
    }

    public void setEmpleos(List<Empleo> nuevosEmpleos) {
        this.lista = nuevosEmpleos;
        notifyDataSetChanged(); // Esto le dice al RecyclerView: "¡Oye, llegaron datos nuevos!"
    }
}