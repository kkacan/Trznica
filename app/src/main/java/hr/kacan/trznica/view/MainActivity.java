package hr.kacan.trznica.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;


import hr.kacan.trznica.R;
import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.viewmodel.PonudaViewModel;

public class MainActivity extends AppCompatActivity {

    private PonudaViewModel model;

    public PonudaViewModel getModel(){
        return this.model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = new ViewModelProvider(this).get(PonudaViewModel.class);
        setTitle(Constants.TIP_PROIZVODA_NAZIV);
        read();
    }


    public void read(){
        setFragment( new PonudaFragment());
    }

    public void cud(){
        setFragment(new CUDFragment());
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }


}