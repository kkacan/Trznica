package hr.kacan.trznica.view;

import hr.kacan.trznica.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.models.Ponuda;
import hr.kacan.trznica.models.ResponsePonuda;
import hr.kacan.trznica.models.TipProizvoda;
import hr.kacan.trznica.viewmodel.PonudaViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class CUDFragment extends Fragment {

    static final int TAKE_PHOTO =1;

    private String photoPath;
    private File slika;
    private Uri slikaURI;

    private PonudaViewModel model;
    //private List<Integer> tip_proizvoda_id;
    //private List<String> tip_proizvoda_naziv;
   // private List<TipProizvoda> tip_proizvoda;
    private Spinner tipProizvodaSpinner;
    private EditText nazivProizvoda;
    private EditText cijenaProizvoda;
    private EditText opisProizvoda;
    private ImageView slikaProizvoda;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabSave;
    private TextView labelNaziv;
    private TextView labelCijena;
    private TextView labelGrad;
    private TextView labelTel;
    private TextView labelOpis;
    private Button btnObrisi;
    private ProgressBar loadingProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cud, container, false);

        tipProizvodaSpinner = view.findViewById(R.id.tip_ponude);
        nazivProizvoda = view.findViewById(R.id.naziv);
        cijenaProizvoda = view.findViewById(R.id.cijena);
        opisProizvoda = view.findViewById(R.id.opis);
        slikaProizvoda = view.findViewById(R.id.image);
        fabEdit = view.findViewById(R.id.fabEdit);
        fabSave = view.findViewById(R.id.fabSave);
        labelNaziv = view.findViewById(R.id.nazivProizvoda);
        labelCijena = view.findViewById(R.id.cijenaProizvoda);
        labelGrad= view.findViewById(R.id.grad);
        labelTel= view.findViewById(R.id.tel);
        labelOpis= view.findViewById(R.id.label_opis);
        btnObrisi = view.findViewById(R.id.delete);
        loadingProgressBar = view.findViewById(R.id.loading);

        model = ((MainActivity) getActivity()).getModel();
        /*tip_proizvoda_id = new ArrayList<>();
        tip_proizvoda_naziv = new ArrayList<>();*/


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        back();
                        return true;
                    }
                }
                return false;
            }
        });

        setView();

        if (model.getPonuda().getNaziv().isEmpty()) {
            setNewPonuda();
            //return view;
        }



        return view;
    }

    private void setView() {

        //getTipProizvoda();

        tipProizvodaSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Constants.TIP_PROIZVODA_LIST));

        Picasso.get()
            .load(model.getPonuda().getSlika())
            .error(R.drawable.no_img)
            .into(slikaProizvoda);

        labelNaziv.setText(model.getPonuda().getNaziv());
        labelCijena.setText(String.format("%.2f", model.getPonuda().getCijena())+getString(R.string.kuna));
        labelGrad.setText(model.getPonuda().getGrad());
        labelTel.setText(model.getPonuda().getTel());
        labelOpis.setText(model.getPonuda().getOpis());

        tipProizvodaSpinner.setVisibility(View.GONE);
        nazivProizvoda.setVisibility(View.GONE);
        cijenaProizvoda.setVisibility(View.GONE);
        opisProizvoda.setVisibility(View.GONE);


        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditPonuda();
                ((MainActivity) getActivity()).setTitle(getActivity().getString(R.string.izmjena));
            }
        });
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPonuda();
            }
        });
        btnObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delPonuda();
            }
        });


        if (!Constants.KORISNIK.getEmail().equalsIgnoreCase(model.getPonuda().getEmail())){
            fabEdit.setVisibility(View.GONE);
        } else {
            fabEdit.setVisibility(View.VISIBLE);
        }


    }

    /*private void getTipProizvoda(){

        model.getTipProizvodaData().observe(getViewLifecycleOwner(), new Observer<List<TipProizvoda>>() {

            @Override
            public void onChanged(List<TipProizvoda> tipProizvoda) {

                tipProizvodaSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Constants.TIP_PROIZVODA_LIST));
                tip_proizvoda = tipProizvoda;


            }
        });

    }*/

    private void setEditPonuda(){

        setNewPonuda();

        int pos=0;
        for (int i = 0; i < Constants.TIP_PROIZVODA_LIST.size() ; i++) {
            if (Constants.TIP_PROIZVODA_LIST.get(i).getId() ==  model.getPonuda().getTipProizvoda()) pos = i;
        }
        tipProizvodaSpinner.setSelection(pos);
        nazivProizvoda.setText(model.getPonuda().getNaziv());
        cijenaProizvoda.setText(String.valueOf(model.getPonuda().getCijena()));
        opisProizvoda.setText(model.getPonuda().getOpis());

    }
    private void editPonuda() {

        back();
    }

    private void setNewPonuda() {

        labelNaziv.setVisibility(View.GONE);
        labelOpis.setVisibility(View.GONE);
        labelTel.setVisibility(View.GONE);
        labelGrad.setVisibility(View.GONE);
        labelCijena.setVisibility(View.GONE);
        fabEdit.setVisibility(View.GONE);
        fabSave.setVisibility(View.VISIBLE);

        tipProizvodaSpinner.setVisibility(View.VISIBLE);
        nazivProizvoda.setVisibility(View.VISIBLE);
        cijenaProizvoda.setVisibility(View.VISIBLE);
        opisProizvoda.setVisibility(View.VISIBLE);
        if (!model.getPonuda().getNaziv().isEmpty()) btnObrisi.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(model.getPonuda().getSlika())
                .error(R.drawable.take_photo)
                .into(slikaProizvoda);
        slikaProizvoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        ((MainActivity) getActivity()).setTitle(getActivity().getString(R.string.novi_proizvod));

        setTextChangedListener();

    }

    private void setTextChangedListener() {
        fabSave.setEnabled(false);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (nazivProizvoda.getText().toString().isEmpty() |
                    cijenaProizvoda.getText().toString().isEmpty() |
                    opisProizvoda.getText().toString().isEmpty()) {
                    fabSave.setEnabled(false);
                } else {
                    fabSave.setEnabled(true);
                }
            }
        };
        nazivProizvoda.addTextChangedListener(afterTextChangedListener);
        cijenaProizvoda.addTextChangedListener(afterTextChangedListener);
        opisProizvoda.addTextChangedListener(afterTextChangedListener);

    }

    private void newPonuda() {

        model.getPonuda().setTipProizvoda(((TipProizvoda)tipProizvodaSpinner.getSelectedItem()).getId());
        model.getPonuda().setNaziv(nazivProizvoda.getText().toString());
        model.getPonuda().setCijena(Double.parseDouble(cijenaProizvoda.getText().toString()));
        model.getPonuda().setOpis(opisProizvoda.getText().toString());
        model.getPonuda().setEmail(Constants.KORISNIK.getEmail());
        model.getPonuda().setGrad(Constants.KORISNIK.getGrad());
        model.getPonuda().setIme(Constants.KORISNIK.getIme());
        model.getPonuda().setPrezime(Constants.KORISNIK.getPrezime());
        model.getPonuda().setTel(Constants.KORISNIK.getTel());
        model.getPonuda().setKorisnikId(Constants.KORISNIK.getId());
        addPonuda();

    }

    private void delPonuda() {

        model.delPonuda(model.getPonuda()).observe(getViewLifecycleOwner(), new Observer<ResponsePonuda>() {

            @Override
            public void onChanged(ResponsePonuda responsePonuda) {
                back();
                System.out.println("GOTCHA DELETE "+ responsePonuda.getResponse());
                System.out.println("GOTCHA DELETE IMAGE "+ responsePonuda.getResponseImage());
            }
        });

    }


    public void back() {
        ((MainActivity) getActivity()).read();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) == null) {
            Toast.makeText(getActivity(), "Problem kod kreiranja slike", Toast.LENGTH_LONG).show();
            return;

        }

            slika = null;
            try {
                slika = kreirajDatotekuSlike();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Problem kod kreiranja slike", Toast.LENGTH_LONG).show();
            return;
            }

            if (slika == null) {
                Toast.makeText(getActivity(), "Problem kod kreiranja slike", Toast.LENGTH_LONG).show();
                return;
            }

            slikaURI = FileProvider.getUriForFile(getActivity(),"hr.kacan.trznica.provider", slika);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, slikaURI);
            startActivityForResult(takePictureIntent, TAKE_PHOTO);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            Picasso.get()
                    .load("file://" + photoPath)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.take_photo)
                    .into(slikaProizvoda);
            if (slika != null) {
                model.getPonuda().setSlika(Constants.IMAGE_PREFIX + slika.getName());
            }


        }
    }

    private File kreirajDatotekuSlike() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imeSlike = "trznica_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imeSlike,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        photoPath = image.getAbsolutePath();
        return image;
    }

    private void addPonuda(){

        loadingProgressBar.setVisibility(View.VISIBLE);
        MultipartBody.Part image = null;
        if (slikaURI != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(slikaURI)), slika);
            image = MultipartBody.Part.createFormData("file", slika.getName(), requestFile);
        }

        model.addPonuda(image, model.getPonuda()).observe(getViewLifecycleOwner(), new Observer<ResponsePonuda>() {
            @Override
            public void onChanged(ResponsePonuda responsePonuda) {
                System.out.println("GOTCHA IMAGE RESPONSE "+ responsePonuda.getResponseImage());
                System.out.println("GOTCHA RESPONSE "+ responsePonuda.getResponse());
                Constants.TIP_PROIZVODA_ID = model.getPonuda().getTipProizvoda();
                loadingProgressBar.setVisibility(View.GONE);
                back();
            }


        });


    }




}