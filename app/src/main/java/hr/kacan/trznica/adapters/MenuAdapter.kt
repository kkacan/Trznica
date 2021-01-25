package hr.kacan.trznica.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.kacan.trznica.R
import hr.kacan.trznica.models.TipProizvoda
import java.util.*

class MenuAdapter(items: MutableList<TipProizvoda>, listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<TipProizvoda> = ArrayList()
    private var mOnItemClickListener: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(view: View, tipProizvoda: TipProizvoda, position: Int)
    }
    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView = v.findViewById<View>(R.id.slika) as ImageView
        var title: TextView = v.findViewById<View>(R.id.naziv) as TextView
        var lyt_parent: View = v.findViewById<View>(R.id.lyt_parent) as View
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.tip_card, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OriginalViewHolder) {
            val tip = items[position]
            holder.title.text = tip.naziv
            Picasso.get()
                    .load(tip.slika)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.no_img)
                    .into(holder.image)

            holder.lyt_parent.setOnClickListener{ view ->
                mOnItemClickListener.onItemClick(view, items[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    init {
        this.items = items

    }

}