package hr.kacan.trznica.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.interfaces.PonudaClickListener;
import hr.kacan.trznica.R;
import hr.kacan.trznica.adapters.PonudaAdapter;
import hr.kacan.trznica.models.Ponuda;
import hr.kacan.trznica.viewmodel.PonudaViewModel;


public class PonudaFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton fab;
    ListView listView;
    TextView noData;

    private PonudaViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ponuda, container, false);

        listView = view.findViewById(R.id.lista);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        fab = view.findViewById(R.id.fab);
        noData = view.findViewById(R.id.nodata);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setPonuda(new Ponuda(0, "", "", "/", 0, Constants.KORISNIK.getId()));
                //System.out.println("GOTCHA "+model.getPonuda().getId());
                ((MainActivity) getActivity()).cud();
            }
        });


        model = ((MainActivity)getActivity()).getModel();

        setList();
        setSwipe();
        refreshData();

        ((MainActivity) getActivity()).setTitle(Constants.TIP_PROIZVODA_NAZIV);
        return view;
    }

    private void refreshData(){
        model.getPonude(Constants.TIP_PROIZVODA_ID).observe(getViewLifecycleOwner(), new Observer<List<Ponuda>>() {


            @Override
            public void onChanged(@Nullable List<Ponuda> ponude) {
                 swipeRefreshLayout.setRefreshing(false);

                ((PonudaAdapter) listView.getAdapter()).setData(ponude);
                ((PonudaAdapter) listView.getAdapter()).updateAdapter();
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

        listView.setAdapter( new PonudaAdapter(getActivity(), R.layout.card_view, new PonudaClickListener() {
            @Override
            public void onItemClick(Ponuda ponuda) {
                model.setPonuda(ponuda);
                //System.out.println("GOTCHA TIP "+ponuda.getTipProizvoda());
                ((MainActivity)getActivity()).cud();
            }
        }));
    }


    public void newPonuda(){
       // model.newPonuda(new Ponuda());
        //((MainActivity)getActivity()).cud();
    }




}