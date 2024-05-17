package it.unimib.sportq.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import it.unimib.sportq.R;
import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.util.BitmapUtils;


public class AllStadiumsAdapter extends
        RecyclerView.Adapter<AllStadiumsAdapter.SearchViewHolder> {

    public interface OnItemClickListener{
        void onStadioItemClick(Stadio listaStadi);
    }
    private List<Stadio> listaStadi;

    public final OnItemClickListener onItemClickListener;


    public AllStadiumsAdapter(List<Stadio> listaStadi, OnItemClickListener onItemClickListener){
        this.listaStadi = listaStadi;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_field, parent, false);
        return new SearchViewHolder(view);
    }

    //association between data and layout
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(listaStadi.get(position));
    }

    @Override
    public int getItemCount() {
        if (listaStadi != null){
            return listaStadi.size();
        }
        return 0;
    }

    //ViewHolder to bind data to the RecyclerView items
    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ShapeableImageView imageView;
        private final TextView textViewName;
        private final TextView textViewAddress;

        public SearchViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.view_image);
            textViewName = itemView.findViewById(R.id.nome_stadio);
            textViewAddress = itemView.findViewById(R.id.indirizzo_stadio);
            itemView.setOnClickListener(this);
        }

        //method bind for binding the cell with the layout
        public void bind(Stadio stadio){
            //qui devo associare i valori che voglio mostrare
            imageView.setImageBitmap(BitmapUtils.stringToBitmap(stadio.getFotoStadio()));
            textViewName.setText(stadio.getStadiumName());
            textViewAddress.setText(stadio.getAddress());
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onStadioItemClick(listaStadi.get(getAdapterPosition()));
        }
    }
}

