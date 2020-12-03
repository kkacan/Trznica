package hr.kacan.trznica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hr.kacan.trznica.R;
import hr.kacan.trznica.interfaces.PonudaClickListener;
import hr.kacan.trznica.models.Ponuda;


public class PonudaAdapter extends RecyclerView.Adapter<PonudaAdapter.Row> {

    private List<Ponuda> ponude;
    private PonudaClickListener ponudaClickListener;
    private LayoutInflater layoutInflater;
    private final Context context;


    public PonudaAdapter(@NonNull Context context) {

        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public PonudaAdapter.Row onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(
                R.layout.card_view, parent, false);
        return new Row(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PonudaAdapter.Row holder, int position) {
        Ponuda ponuda = ponude.get(position);
        holder.naziv.setText(ponuda.getNaziv());
        holder.cijena.setText(String.format("%.2f", ponuda.getCijena()) + context.getString(R.string.kuna));
        holder.grad.setText(ponuda.getGrad());
        holder.tel.setText(ponuda.getTel());
        Picasso.get()
                .load(ponuda.getSlika())
                .fit()
                .centerCrop()
                .error(R.drawable.no_img)
                .into(holder.slika);

    }

    @Override
    public int getItemCount() {
        return ponude == null ? 0 : ponude.size();
    }


    public Ponuda getItem(int position) {
        return ponude.get(position);
    }


    public void setData(List<Ponuda> itemList) {
        this.ponude = itemList;
    }

    public class Row extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView naziv;
        private TextView cijena;
        private TextView grad;
        private TextView tel;
        private ImageView slika;

        public Row(View view) {
            super(view);
            naziv = view.findViewById(R.id.naziv);
            cijena = view.findViewById(R.id.cijena);
            grad = view.findViewById(R.id.grad);
            tel = view.findViewById(R.id.tel);
            slika = view.findViewById(R.id.slika);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (ponudaClickListener != null) {
                ponudaClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(PonudaClickListener clickListener) {
        this.ponudaClickListener = clickListener;
    }
}
