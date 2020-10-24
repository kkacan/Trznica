package hr.kacan.trznica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.squareup.picasso.Picasso;
import java.util.List;
import hr.kacan.trznica.interfaces.PonudaClickListener;
import hr.kacan.trznica.R;
import hr.kacan.trznica.models.Ponuda;


public class PonudaAdapter extends ArrayAdapter<Ponuda> {

    private List<Ponuda> ponude;
    private final PonudaClickListener ponudaClickListener;
    private final int resource;
    private final Context context;


    public PonudaAdapter(@NonNull Context context, int resource, PonudaClickListener ponudaClickListener) {
        super(context, resource);

        this.ponudaClickListener = ponudaClickListener;
        this.resource = resource;
        this.context = context;
    }

    private static class ViewHolder {

        private TextView naziv;
        private TextView cijena;
        private TextView grad;
        private TextView tel;
        private ImageView slika;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Ponuda ponuda;

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.resource, null);
            viewHolder.naziv = view.findViewById(R.id.naziv);
            viewHolder.cijena = view.findViewById(R.id.cijena);
            viewHolder.grad = view.findViewById(R.id.grad);
            viewHolder.tel = view.findViewById(R.id.tel);
            viewHolder.slika = view.findViewById(R.id.slika);
        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

        ponuda = getItem(position);

        if (ponuda != null & viewHolder != null) {
            viewHolder.naziv.setText(ponuda.getNaziv());
            viewHolder.cijena.setText(String.format("%.2f", ponuda.getCijena())+context.getString(R.string.kuna));
            viewHolder.grad.setText(ponuda.getGrad());
            viewHolder.tel.setText(ponuda.getTel());

            Picasso.get()
                    .load(ponuda.getSlika())
                    .fit()
                    .centerCrop()
                    .error(R.drawable.no_img)
                    .into(viewHolder.slika);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ponudaClickListener.onItemClick(ponuda);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return ponude == null ? 0 : ponude.size();
    }

    @Nullable
    @Override
    public Ponuda getItem(int position) {
        return ponude.get(position);
    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }

    public void setData(List<Ponuda> itemList) {
        this.ponude = itemList;
    }
}
