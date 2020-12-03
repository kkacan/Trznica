package hr.kacan.trznica.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.kacan.trznica.R;
import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.models.ResponsePonuda;
import hr.kacan.trznica.models.TipProizvoda;
import hr.kacan.trznica.viewmodel.PonudaViewModel;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class CUDFragment extends Fragment {

    private static final int TAKE_PHOTO =1;
    private static final int RESULT_LOAD_IMG = 2;
    private static final int PERMISSION_REQUEST = 3;

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
                photoDialog();
            }
        });

        ((MainActivity) getActivity()).setTitle(getActivity().getString(R.string.novi_proizvod));

        setTextChangedListener();

    }

    private void photoDialog(){

        final AlertDialog dialog;

        if (!getActivity().isFinishing()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View back_dialog = inflater.inflate(R.layout.photo_dialog, null);
            alertDialog.setView(back_dialog).setCancelable(true);
            dialog = alertDialog.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button btn_photo = back_dialog.findViewById(R.id.btn_photo);
            Button btn_gallery = back_dialog.findViewById(R.id.btn_gallery);

            btn_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            reqRunTimePermissions();
                        } else {
                            fromGallery();
                        }
                    } else {
                        fromGallery();
                    }

                    dialog.cancel();

                }
            });
            btn_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    takePhoto();
                }
            });


        }


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

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.del_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        model.delPonuda(model.getPonuda()).observe(getViewLifecycleOwner(), new Observer<ResponsePonuda>() {

                            @Override
                            public void onChanged(ResponsePonuda responsePonuda) {

                                if (responsePonuda.getResponse().equals(Constants.RESPONSE_SUCCESS)){
                                    Toast.makeText(getActivity(), getString(R.string.del_success_msg), Toast.LENGTH_LONG).show();
                                    back();
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.del_fail_msg), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
        alertDialog.show();

    }


    public void back() {
        ((MainActivity) getActivity()).read();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) == null) {
            Toast.makeText(getActivity(), getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show();
            return;

        }

            slika = null;
            try {
                slika = kreirajDatotekuSlike();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show();
            return;
            }

            if (slika == null) {
                Toast.makeText(getActivity(), getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show();
                return;
            }

            slikaURI = FileProvider.getUriForFile(getActivity(),"hr.kacan.trznica.provider", slika);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, slikaURI);
            startActivityForResult(takePictureIntent, TAKE_PHOTO);

    }

    private void fromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            try {
                slika =new Compressor(getActivity())
                        .setMaxWidth(800)
                        .setMaxHeight(600)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(slika);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show();
            }

            setImage();

        }

        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK) {

            slikaURI = data.getData();
            File tmpFile = null;
            if (data.getData() != null) {
                tmpFile = new File(getPath(slikaURI));
                photoPath = tmpFile.getAbsolutePath();
            }
            try {
                    slika =new Compressor(getActivity())
                            .setMaxWidth(800)
                            .setMaxHeight(600)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(tmpFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show();
                }

            setImage();

        }

    }

    public String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void setImage(){
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
                if (responsePonuda.getResponse().equals(Constants.RESPONSE_SUCCESS)){
                    Toast.makeText(getActivity(), getString(R.string.add_success_msg), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.add_fail_msg), Toast.LENGTH_LONG).show();
                }
                Constants.TIP_PROIZVODA_ID = model.getPonuda().getTipProizvoda();
                loadingProgressBar.setVisibility(View.GONE);
                back();
            }


        });

    }

    public void reqRunTimePermissions() {

        if (!getActivity().isFinishing()) {
            int permission = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                    //builder.setMessage("Permission")
                    builder.setTitle(getString(R.string.perm_write));

                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            makeRequest();
                        }
                    });

                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    makeRequest();
                }
            }
        }
    }

    protected void makeRequest() {
        requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        boolean permission_w;
        if (requestCode == PERMISSION_REQUEST) {
            permission_w = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permission_w){
                fromGallery();
            }
        }
    }


}