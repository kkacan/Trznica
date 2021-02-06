package hr.kacan.trznica.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.kacan.trznica.R
import hr.kacan.trznica.interfaces.PonudaClickListener
import hr.kacan.trznica.models.Ponuda

class PonudaAdapter(private val context: Context, listener: PonudaClickListener) : RecyclerView.Adapter<PonudaAdapter.Row>()   {

    private var ponude: MutableList<Ponuda>? = null
    private var ponudaClickListener: PonudaClickListener = listener
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Row {
        val view = layoutInflater.inflate(R.layout.card_view, parent, false)
        return Row(view)
    }

    override fun onBindViewHolder(holder: Row, position: Int) {
        val ponuda = ponude?.get(position)
        holder.naziv.text = ponuda?.naziv
        holder.cijena.text = String.format("%.2f", ponuda?.cijena) + context.getString(R.string.kuna)
        holder.grad.text = ponuda?.grad
        holder.tel.text = ponuda?.tel
        Picasso.get()
                .load(ponuda?.slika)
                .fit()
                .centerCrop()
                .error(R.drawable.no_img)
                .into(holder.slika)

        holder.lyt_parent.setOnClickListener { view ->
            ponudaClickListener.onItemClick(view, position)
        }
    }

    override fun getItemCount(): Int {
        return ponude?.size ?: 0
    }

    fun getItem(position: Int): Ponuda {
        return ponude!![position]
    }

    fun setData(itemList: MutableList<Ponuda>) {
        ponude = itemList
    }

    inner class Row(view: View) : RecyclerView.ViewHolder(view) {

        var lyt_parent: View = view.findViewById(R.id.lyt_parent) as View
        val naziv: TextView = view.findViewById(R.id.naziv)
        val cijena: TextView = view.findViewById(R.id.cijena)
        val grad: TextView = view.findViewById(R.id.grad)
        val tel: TextView = view.findViewById(R.id.tel)
        val slika: ImageView = view.findViewById(R.id.slika)

    }

}