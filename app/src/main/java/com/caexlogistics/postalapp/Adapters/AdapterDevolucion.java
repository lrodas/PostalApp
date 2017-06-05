package com.caexlogistics.postalapp.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caexlogistics.postalapp.Models.MovilDevolucion;
import com.caexlogistics.postalapp.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Usuario on 4/06/2017.
 */

public class AdapterDevolucion extends RecyclerView.Adapter<AdapterDevolucion.ViewHolder> {

    private List<MovilDevolucion> listDevolucion;
    private int layout;
    private AdapterDevolucion.OnItemClickListener onItemClickListener;

    public AdapterDevolucion(Activity activity, List<MovilDevolucion> listDevolucion, int layout, AdapterDevolucion.OnItemClickListener onItemClickListener) {
        this.listDevolucion = listDevolucion;
        this.layout = layout;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AdapterDevolucion.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterDevolucion.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterDevolucion.ViewHolder holder, int position) {
        holder.bind(listDevolucion.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return listDevolucion.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lblItemPiezaDevolucion;
        public TextView lblItemFechaDevolucion;
        public TextView lblItemTipoDevolucion;
        public TextView lblItemCarteroDevolucion;
        public TextView lblItemRutaDevolucion;


        public ViewHolder(View itemView) {
            super(itemView);
            lblItemPiezaDevolucion = (TextView) itemView.findViewById(R.id.lblItemPiezaDevolucion);
            lblItemFechaDevolucion = (TextView) itemView.findViewById(R.id.lblItemFechaDevolucion);
            lblItemTipoDevolucion = (TextView) itemView.findViewById(R.id.lblItemTipoDevolucion);
            lblItemCarteroDevolucion = (TextView) itemView.findViewById(R.id.lblItemCarteroDevolucion);
            lblItemRutaDevolucion = (TextView) itemView.findViewById(R.id.lblItemRutaDevolucion);
        }

        public void bind(final MovilDevolucion movilDevolucion, final AdapterDevolucion.OnItemClickListener onItemClickListener){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            lblItemPiezaDevolucion.setText(movilDevolucion.getPieza());
            lblItemFechaDevolucion.setText(format.format(movilDevolucion.getFechaDevolucion()));
            lblItemTipoDevolucion.setText(movilDevolucion.getTipoDevolucion().getDescripcion());
            lblItemCarteroDevolucion.setText(movilDevolucion.getCodigoCartero());
            lblItemRutaDevolucion.setText(movilDevolucion.getRuta());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(movilDevolucion, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(MovilDevolucion movilDevolucion, int position);
    }
}
