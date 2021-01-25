package hr.kacan.trznica.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.kacan.trznica.App
import hr.kacan.trznica.R
import hr.kacan.trznica.adapters.PonudaAdapter
import hr.kacan.trznica.interfaces.PonudaClickListener
import hr.kacan.trznica.models.Ponuda
import hr.kacan.trznica.viewmodel.PonudaViewModel

class PonudaFragment : Fragment(),PonudaClickListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var noData: TextView
    private lateinit var ponudaAdapter: PonudaAdapter
    private lateinit var model: PonudaViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.fragment_ponuda, container, false)
        recyclerView = view.findViewById(R.id.lista)
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer)
        fab = view.findViewById(R.id.fab)
        noData = view.findViewById(R.id.nodata)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        fab.setOnClickListener {
            model.setPonuda(Ponuda(0.0, "", "", "/", App.TIP_PROIZVODA_ID.toInt(), App.KORISNIK.id))
            (activity as MainActivity).cud()
        }
        model = (activity as MainActivity).getModel()
        setList()
        setSwipe()
        refreshData()

        return view
    }

    private fun refreshData() {
        model.getPonude(App.TIP_PROIZVODA_ID, (activity as MainActivity).search).observe(viewLifecycleOwner, Observer { ponude ->
            swipeRefreshLayout.isRefreshing = false
            ponude.sortByDescending { it.id }
            (recyclerView.adapter as PonudaAdapter).setData(ponude)
            (recyclerView.adapter as PonudaAdapter).notifyDataSetChanged()

            if ((activity as MainActivity).search.isEmpty()) {
                (activity as MainActivity).title = App.TIP_PROIZVODA_NAZIV
            } else {
                (activity as MainActivity).title = "Pretraga: "+(activity as MainActivity).search
            }
            if (ponude != null) {
                if (ponude.size == 0) {
                    noData.visibility = View.VISIBLE
                } else {
                    noData.visibility = View.GONE
                }
            }
        })
    }

    private fun setSwipe() {
        swipeRefreshLayout.setOnRefreshListener { refreshData() }
    }

    private fun setList() {
        ponudaAdapter = PonudaAdapter(activity as AppCompatActivity, this)
        recyclerView.adapter = ponudaAdapter
    }

    override fun onItemClick(view: View, position: Int) {
        model.setPonuda(ponudaAdapter.getItem(position))
        (activity as MainActivity).cud()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = App.TIP_PROIZVODA_NAZIV
    }
}