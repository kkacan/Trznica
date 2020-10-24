package hr.kacan.trznica.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import hr.kacan.trznica.R;
import hr.kacan.trznica.adapters.MenuAdapter;
import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.models.TipProizvoda;
import hr.kacan.trznica.utils.SpacingItemDecoration;
import hr.kacan.trznica.utils.Tools;
import hr.kacan.trznica.viewmodel.TipViewModel;

public class MenuActivity extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;
    private MenuAdapter mAdapter;
    private TipViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        parent_view = findViewById(R.id.parent_view);

        model = new ViewModelProvider(this).get(TipViewModel.class);

        initToolbar();
        initComponent();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                Intent aboutIntent = new Intent(MenuActivity.this, UserActivity.class);
                startActivity(aboutIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);*/
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        setItems();


    }

    private void setItems() {

        model.getTipProizvodaData().observe(this, new Observer<List<TipProizvoda>>() {
            @Override
            public void onChanged(List<TipProizvoda> items) {

                if (items != null) {
                    Constants.TIP_PROIZVODA_LIST = items;
                    TipProizvoda sviProizvodi = new TipProizvoda();
                    sviProizvodi.setId(0);
                    sviProizvodi.setNaziv("Svi proizvodi");
                    sviProizvodi.setSlika("http://ec2-18-185-93-51.eu-central-1.compute.amazonaws.com/images/11.png");
                    items.add(sviProizvodi);
                    mAdapter = new MenuAdapter(MenuActivity.this, items);
                    recyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, TipProizvoda tipProizvoda, int position) {
                            //Snackbar.make(parent_view, "Item " + tipProizvoda.getNaziv() + " clicked", Snackbar.LENGTH_SHORT).show();
                            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                            Constants.TIP_PROIZVODA_ID = tipProizvoda.getId();
                            Constants.TIP_PROIZVODA_NAZIV = tipProizvoda.getNaziv();
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }


}
