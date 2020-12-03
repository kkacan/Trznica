package hr.kacan.trznica.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hr.kacan.trznica.R;
import hr.kacan.trznica.adapters.PonudaAdapter;
import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.interfaces.PonudaClickListener;
import hr.kacan.trznica.models.Ponuda;
import hr.kacan.trznica.viewmodel.PonudaViewModel;


public class PonudaFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    TextView noData;
    PonudaAdapter ponudaAdapter;


    private PonudaViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ponuda, container, false);

        recyclerView = view.findViewById(R.id.lista);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        fab = view.findViewById(R.id.fab);
        noData = view.findViewById(R.id.nodata);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setPonuda(new Ponuda(0, "", "", "/", 0, Constants.KORISNIK.getId()));
                ((MainActivity) getActivity()).cud();
            }
        });

        model = ((MainActivity) getActivity()).getModel();

        setList();
        setSwipe();
        refreshData();

        ((MainActivity) getActivity()).setTitle(Constants.TIP_PROIZVODA_NAZIV);
        return view;
    }

    private void refreshData() {
        model.getPonude(Constants.TIP_PROIZVODA_ID).observe(getViewLifecycleOwner(), new Observer<List<Ponuda>>() {

            @Override
            public void onChanged(@Nullable List<Ponuda> ponude) {
                swipeRefreshLayout.setRefreshing(false);

                ((PonudaAdapter) recyclerView.getAdapter()).setData(ponude);
                ((PonudaAdapter) recyclerView.getAdapter()).notifyDataSetChanged();
                if (ponude != null) {
                    if (ponude.size() == 0) {
                        noData.setVisibility(View.VISIBLE);
                    } else {
                        noData.setVisibility(View.GONE);
                    }
                }

            }
        });

    }

    private void setSwipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

    }

    private void setList() {

        ponudaAdapter = new PonudaAdapter(getActivity());

        ponudaAdapter.setClickListener(new PonudaClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                model.setPonuda(ponudaAdapter.getItem(position));
                //System.out.println("GOTCHA TIP "+ponuda.getTipProizvoda());
                ((MainActivity) getActivity()).cud();
            }
        });
        recyclerView.setAdapter(ponudaAdapter);
    }

}