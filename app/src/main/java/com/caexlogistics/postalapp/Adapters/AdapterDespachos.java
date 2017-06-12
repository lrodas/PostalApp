package com.caexlogistics.postalapp.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caexlogistics.postalapp.Models.MovilDespachos;
import com.caexlogistics.postalapp.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 3plgtluisrc on 17/01/2017.
 */

public class AdapterDespachos extends RecyclerView.Adapter<AdapterDespachos.ViewHolder> {

    private List<MovilDespachos> listDespachos;
    private int layout;
    private OnItemClickListener onItemClickListener;

    public AdapterDespachos(Activity activity, List<MovilDespachos> listDespachos, int layout, OnItemClickListener onItemClickListener) {
        this.listDespachos = listDespachos;
        this.layout = layout;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(listDespachos.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return listDespachos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView lblItemPiezaDespacho;
        public TextView lblItemFechaDespacho;
        public TextView lblItemTipoDespacho;
        public TextView lblItemCicloDespacho;
        public TextView lblItemAgenciaDespacho;
        public TextView lblItemNombreDespacho;
        public TextView lblItemDireccionDespacho;


        public ViewHolder(View itemView) {
            super(itemView);
            lblItemPiezaDespacho = (TextView) itemView.findViewById(R.id.lblItemPiezaDespacho);
            lblItemFechaDespacho = (TextView) itemView.findViewById(R.id.lblItemFechaDespacho);
            lblItemTipoDespacho = (TextView) itemView.findViewById(R.id.lblItemTipoDespacho);
            lblItemCicloDespacho = (TextView) itemView.findViewById(R.id.lblItemCicloDespacho);
            lblItemAgenciaDespacho = (TextView) itemView.findViewById(R.id.lblItemAgenciaDespacho);
            lblItemNombreDespacho = (TextView) itemView.findViewById(R.id.lblItemNombreDespacho);
            lblItemDireccionDespacho = (TextView) itemView.findViewById(R.id.lblItemDireccionDespacho);
        }

        public void bind(final MovilDespachos movilDespachos, final OnItemClickListener onItemClickListener){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            lblItemPiezaDespacho.setText(movilDespachos.getPieza());
            lblItemFechaDespacho.setText(format.format(movilDespachos.getFechaDespacho()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(movilDespachos, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(MovilDespachos movilDespachos, int position);
    }
}
