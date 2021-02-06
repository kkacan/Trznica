package hr.kacan.trznica.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import hr.kacan.trznica.App
import hr.kacan.trznica.R
import hr.kacan.trznica.adapters.MenuAdapter
import hr.kacan.trznica.conf.Constants
import hr.kacan.trznica.models.TipProizvoda
import hr.kacan.trznica.utils.SpacingItemDecoration
import hr.kacan.trznica.utils.Tools
import hr.kacan.trznica.viewmodel.TipViewModel

@AndroidEntryPoint
class MenuActivity() : AppCompatActivity(), MenuAdapter.OnItemClickListener {

    private lateinit var parent_view: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: MenuAdapter
    private val model: TipViewModel by viewModels()
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        parent_view = findViewById(R.id.parent_view)
        initComponent()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.search);
        searchView.setOnQueryTextFocusChangeListener { _, b ->
            searchView.isIconified = !b
        }
        initSearch()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_user -> {
                val aboutIntent = Intent(this@MenuActivity, UserActivity::class.java)
                startActivity(aboutIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initComponent() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.addItemDecoration(SpacingItemDecoration(2, Tools.dpToPx(this, 8), true))
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        setItems()
    }

    private fun setItems() {
        model.getTipProizvodaData()
        model.tipProizvoda.observe(this, { items ->
            if (items != null) {
                App.TIP_PROIZVODA_LIST = items
                val tipProizvodaList: MutableList<TipProizvoda> = items.toMutableList()
                val sviProizvodi = TipProizvoda(0, Constants.SVI_PROIZVODI_NAME, Constants.SVI_PROIZVODI_IMAGE)
                tipProizvodaList.add(sviProizvodi)
                mAdapter = MenuAdapter(tipProizvodaList, this)
                recyclerView.adapter = mAdapter
            }
        })
    }

    override fun onItemClick(view: View, tipProizvoda: TipProizvoda, position: Int) {
        val intent = Intent(this@MenuActivity, MainActivity::class.java)
        App.TIP_PROIZVODA_ID = tipProizvoda.id.toLong()
        App.TIP_PROIZVODA_NAZIV = tipProizvoda.naziv
        startActivity(intent)
    }

    private fun initSearch() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                searchView.clearFocus()
                App.TIP_PROIZVODA_ID = 0
                App.TIP_PROIZVODA_NAZIV = ""
                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                intent.putExtra("search", s)
                startActivity(intent)
                return true
            }
            override fun onQueryTextChange(s: String?): Boolean {
                return true
            }
        })
    }
}