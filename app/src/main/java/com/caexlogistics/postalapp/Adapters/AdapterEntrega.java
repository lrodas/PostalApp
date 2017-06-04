package com.caexlogistics.postalapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caexlogistics.postalapp.Models.MovilEntrega;
import com.caexlogistics.postalapp.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Usuario on 4/06/2017.
 */

public class AdapterEntrega extends RecyclerView.Adapter<AdapterEntrega.ViewHolder> {

    private List<MovilEntrega> listEntrega;
    private int layout;
    private AdapterEntrega.OnItemClickListener onItemClickListener;

    public AdapterEntrega(List<MovilEntrega> listEntrega, int layout, AdapterEntrega.OnItemClickListener onItemClickListener) {
        this.listEntrega = listEntrega;
        this.layout = layout;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(listEntrega.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return listEntrega.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView lblItemPiezaEntrega;
        private TextView lblItemFechaEntrega;
        private TextView lblItemCarteroEntrega;
        private TextView lblItemRutaEntrega;

        public ViewHolder(View itemView) {
            super(itemView);
            lblItemPiezaEntrega = (TextView) itemView.findViewById(R.id.lblItemPiezaEntrega);
            lblItemFechaEntrega = (TextView) itemView.findViewById(R.id.lblItemFechaEntrega);
            lblItemCarteroEntrega = (TextView) itemView.findViewById(R.id.lblItemCarteroEntrega);
            lblItemRutaEntrega = (TextView) itemView.findViewById(R.id.lblItemRutaEntrega);
        }

        public void bind(final MovilEntrega movilEntrega, final AdapterEntrega.OnItemClickListener onItemClickListener){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            lblItemPiezaEntrega.setText(movilEntrega.getPieza());
            lblItemFechaEntrega.setText(format.format(movilEntrega.getFechaEntrega()));
            lblItemCarteroEntrega.setText(movilEntrega.getCodigoCartero());
            lblItemRutaEntrega.setText(movilEntrega.getRuta());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(movilEntrega, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(MovilEntrega movilEntrega, int position);
    }
}
