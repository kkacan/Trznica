package hr.kacan.trznica.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import hr.kacan.trznica.App
import hr.kacan.trznica.R
import hr.kacan.trznica.conf.Constants
import hr.kacan.trznica.models.TipProizvoda
import hr.kacan.trznica.utils.FileHelper
import hr.kacan.trznica.viewmodel.PonudaViewModel
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

open class CUDFragment : Fragment() {

    private lateinit var photoPath: String
    private var slika: File? = null
    private var slikaURI: Uri? = null
    private lateinit var model: PonudaViewModel
    private lateinit var tipProizvodaSpinner: Spinner
    private lateinit var nazivProizvoda: EditText
    private lateinit var cijenaProizvoda: EditText
    private lateinit var opisProizvoda: EditText
    private lateinit var slikaProizvoda: ImageView
    private lateinit var fabEdit: FloatingActionButton
    private lateinit var fabSave: FloatingActionButton
    private lateinit var labelNaziv: TextView
    private lateinit var labelCijena: TextView
    private lateinit var labelGrad: TextView
    private lateinit var labelTel: TextView
    private lateinit var labelOpis: TextView
    private lateinit var btnObrisi: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var btnKontakt: ImageButton

    companion object {
        private const val TAKE_PHOTO = 1
        private const val RESULT_LOAD_IMG = 2
        private const val PERMISSION_REQUEST = 3
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_cud, container, false)
        tipProizvodaSpinner = view.findViewById(R.id.tip_ponude)
        nazivProizvoda = view.findViewById(R.id.naziv)
        cijenaProizvoda = view.findViewById(R.id.cijena)
        opisProizvoda = view.findViewById(R.id.opis)
        slikaProizvoda = view.findViewById(R.id.image)
        fabEdit = view.findViewById(R.id.fabEdit)
        fabSave = view.findViewById(R.id.fabSave)
        labelNaziv = view.findViewById(R.id.nazivProizvoda)
        labelCijena = view.findViewById(R.id.cijenaProizvoda)
        labelGrad = view.findViewById(R.id.grad)
        labelTel = view.findViewById(R.id.tel)
        labelOpis = view.findViewById(R.id.label_opis)
        btnObrisi = view.findViewById(R.id.delete)
        loadingProgressBar = view.findViewById(R.id.loading)
        btnKontakt = view.findViewById(R.id.kontakt)
        model = (activity as MainActivity).getModel()
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    back()
                    return@OnKeyListener true
                }
            }
            false
        })

        setView()
        if (model.getPonuda().naziv.isEmpty()) {
            setNewPonuda()
        }
        return view
    }

    private fun setView() {

        tipProizvodaSpinner.adapter = ArrayAdapter(activity as AppCompatActivity, android.R.layout.simple_list_item_1, App.TIP_PROIZVODA_LIST)
        Picasso.get()
                .load(model.getPonuda().slika)
                .error(R.drawable.no_img)
                .into(slikaProizvoda)
        labelNaziv.text = model.getPonuda().naziv
        labelCijena.text = String.format("%.2f", model.getPonuda().cijena) + getString(R.string.kuna)
        labelGrad.text = model.getPonuda().grad
        labelTel.text = model.getPonuda().tel
        labelOpis.text = model.getPonuda().opis
        tipProizvodaSpinner.visibility = View.GONE
        nazivProizvoda.visibility = View.GONE
        cijenaProizvoda.visibility = View.GONE
        opisProizvoda.visibility = View.GONE
        fabEdit.setOnClickListener {
            setEditPonuda()
            (activity as AppCompatActivity).title = (activity as AppCompatActivity).getString(R.string.izmjena)
        }
        fabSave.setOnClickListener { newPonuda() }
        btnObrisi.setOnClickListener { delPonuda() }
        if (!App.KORISNIK.email.equals(model.getPonuda().email, ignoreCase = true)) {
            fabEdit.visibility = View.GONE
            btnKontakt.visibility = View.VISIBLE

        } else {
            fabEdit.visibility = View.VISIBLE
            btnKontakt.visibility = View.GONE
        }

        btnKontakt.setOnClickListener {
            kontaktDialog()
        }
    }



    private fun setEditPonuda() {
        setNewPonuda()
        var pos = 0
        for (i in App.TIP_PROIZVODA_LIST.indices) {
            if (App.TIP_PROIZVODA_LIST[i].id  == model.getPonuda().tipProizvoda) pos = i
        }
        tipProizvodaSpinner.setSelection(pos)
        nazivProizvoda.setText(model.getPonuda().naziv)
        cijenaProizvoda.setText(model.getPonuda().cijena.toString())
        opisProizvoda.setText(model.getPonuda().opis)
    }

    private fun setNewPonuda() {
        labelNaziv.visibility = View.GONE
        labelOpis.visibility = View.GONE
        labelTel.visibility = View.GONE
        labelGrad.visibility = View.GONE
        labelCijena.visibility = View.GONE
        fabEdit.visibility = View.GONE
        btnKontakt.visibility = View.GONE
        fabSave.visibility = View.VISIBLE
        tipProizvodaSpinner.visibility = View.VISIBLE
        nazivProizvoda.visibility = View.VISIBLE
        cijenaProizvoda.visibility = View.VISIBLE
        opisProizvoda.visibility = View.VISIBLE
        if (model.getPonuda().naziv.isNotEmpty()) btnObrisi.visibility = View.VISIBLE
        Picasso.get()
                .load(model.getPonuda().slika)
                .error(R.drawable.take_photo)
                .into(slikaProizvoda)
        slikaProizvoda.setOnClickListener { photoDialog() }
        (activity as AppCompatActivity).title = (activity as AppCompatActivity).getString(R.string.novi_proizvod)
        setTextChangedListener()

        var pos = 0
        for (i in App.TIP_PROIZVODA_LIST.indices) {
            if (App.TIP_PROIZVODA_LIST[i].id  == model.getPonuda().tipProizvoda) pos = i
        }
        tipProizvodaSpinner.setSelection(pos)
    }

    private fun photoDialog() {
        val dialog: AlertDialog
        if (!(activity as AppCompatActivity).isFinishing) {
            val alertDialog = AlertDialog.Builder(activity)
            val inflater = (activity as AppCompatActivity).layoutInflater
            val backDialog = inflater.inflate(R.layout.photo_dialog, null)
            alertDialog.setView(backDialog).setCancelable(true)
            dialog = alertDialog.create()
            dialog.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnPhoto = backDialog.findViewById<Button>(R.id.btn_photo)
            val btnGallery = backDialog.findViewById<Button>(R.id.btn_gallery)
            btnGallery.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission((activity as AppCompatActivity), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        reqRunTimePermissions()
                    } else {
                        fromGallery()
                    }
                } else {
                    fromGallery()
                }
                dialog.cancel()
            }
            btnPhoto.setOnClickListener {
                dialog.cancel()
                takePhoto()
            }
        }
    }

    private fun setTextChangedListener() {
        fabSave.isEnabled = false
        val afterTextChangedListener: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                fabSave.isEnabled = !(nazivProizvoda.text.toString().isEmpty() or
                        cijenaProizvoda.text.toString().isEmpty() or
                        opisProizvoda.text.toString().isEmpty())
            }
        }
        nazivProizvoda.addTextChangedListener(afterTextChangedListener)
        cijenaProizvoda.addTextChangedListener(afterTextChangedListener)
        opisProizvoda.addTextChangedListener(afterTextChangedListener)
    }

    private fun newPonuda() {
        model.getPonuda().tipProizvoda = (tipProizvodaSpinner.selectedItem as TipProizvoda).id
        model.getPonuda().naziv = nazivProizvoda.text.toString()
        model.getPonuda().cijena = cijenaProizvoda.text.toString().toDouble()
        model.getPonuda().opis = opisProizvoda.text.toString()
        model.getPonuda().email = App.KORISNIK.email
        model.getPonuda().grad = App.KORISNIK.grad
        model.getPonuda().ime = App.KORISNIK.ime
        model.getPonuda().prezime = App.KORISNIK.prezime
        model.getPonuda().tel = App.KORISNIK.tel
        model.getPonuda().korisnikId = App.KORISNIK.id
        addPonuda()
    }

    private fun delPonuda() {
        val alertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setTitle(getString(R.string.del_message))
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok)
        ) { dialog, _ ->
            model.delPonuda(model.getPonuda()).observe(viewLifecycleOwner, Observer { responsePonuda ->
                if (responsePonuda.response == Constants.RESPONSE_SUCCESS) {
                    Toast.makeText(activity, getString(R.string.del_success_msg), Toast.LENGTH_LONG).show()
                    back()
                } else {
                    Toast.makeText(activity, getString(R.string.del_fail_msg), Toast.LENGTH_LONG).show()
                }
            })
            dialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun back() {
        (activity as MainActivity).read()
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity((activity as AppCompatActivity).packageManager) == null) {
            Toast.makeText(activity, getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show()
            return
        }

        slika = try {
            kreirajDatotekuSlike()
        } catch (ex: IOException) {
            Toast.makeText(activity, getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show()
            return
        }
        slikaURI = FileProvider.getUriForFile((activity as AppCompatActivity), "hr.kacan.trznica.provider", slika!!)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, slikaURI)
        startActivityForResult(takePictureIntent, TAKE_PHOTO)
    }

    private fun fromGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            try {
                slika = Compressor((activity as AppCompatActivity))
                        .setMaxWidth(800)
                        .setMaxHeight(600)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(slika)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText((activity as AppCompatActivity), getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show()
            }
            setImage()
        }
        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK) {
            slikaURI = intent?.data!!
            var tmpFile: File? = null
            if (intent.data != null) {
                val fileHelper = FileHelper(activity as AppCompatActivity)
                tmpFile = File(fileHelper.getPath(slikaURI!!))
                photoPath = tmpFile.absolutePath
            }
            try {
                slika = Compressor((activity as AppCompatActivity))
                        .setMaxWidth(800)
                        .setMaxHeight(600)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(tmpFile)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText((activity as AppCompatActivity), getString(R.string.slika_error_msg), Toast.LENGTH_LONG).show()
            }
            setImage()
        }
    }

    private fun setImage() {
        Picasso.get()
                .load("file://$photoPath")
                .fit()
                .centerCrop()
                .error(R.drawable.take_photo)
                .into(slikaProizvoda)
        model.getPonuda().slika = Constants.IMAGE_PREFIX + slika?.name
    }

    @Throws(IOException::class)
    private fun kreirajDatotekuSlike(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imeSlike = "trznica_" + timeStamp + "_"
        val storageDir = (activity as AppCompatActivity).getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imeSlike,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        photoPath = image.absolutePath
        return image
    }

    private fun addPonuda() {
        loadingProgressBar.visibility = View.VISIBLE
        var image: MultipartBody.Part? = null

        if (slikaURI != null && slika != null) {
            val requestFile = RequestBody.create(MediaType.parse((activity as AppCompatActivity).contentResolver.getType(slikaURI!!)), slika)
            image = MultipartBody.Part.createFormData("file", slika?.name, requestFile)
        }
        model.addPonuda(image, model.getPonuda()).observe(viewLifecycleOwner, Observer { responsePonuda ->
            if (responsePonuda.response == Constants.RESPONSE_SUCCESS) {
                Toast.makeText(activity, getString(R.string.add_success_msg), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, getString(R.string.add_fail_msg), Toast.LENGTH_LONG).show()
            }
            App.TIP_PROIZVODA_ID = model.getPonuda().tipProizvoda.toLong()
            loadingProgressBar.visibility = View.GONE
            App.TIP_PROIZVODA_NAZIV = (tipProizvodaSpinner.selectedItem as TipProizvoda).naziv
            back()
        })
    }

    private fun reqRunTimePermissions() {
        if (!(activity as AppCompatActivity).isFinishing) {
            val permission = ContextCompat.checkSelfPermission((activity as AppCompatActivity),
                    Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    val builder = androidx.appcompat.app.AlertDialog.Builder((activity as AppCompatActivity))
                    //builder.setMessage("Permission")
                    builder.setTitle(getString(R.string.perm_write))
                    builder.setPositiveButton(getString(R.string.ok)) { _, _ -> makeRequest() }
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    makeRequest()
                }
            }
        }
    }

    private fun makeRequest() {
        requestPermissions(arrayOf<String?>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        val permission_w: Boolean
        if (requestCode == PERMISSION_REQUEST) {
            permission_w = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (permission_w) {
                fromGallery()
            }
        }
    }

    private fun kontaktDialog() {
        val layoutInflater = LayoutInflater.from(activity as AppCompatActivity)
        val promptView: View = layoutInflater.inflate(R.layout.contact_dialog, null)
        val contactDialog = AlertDialog.Builder(activity as AppCompatActivity).create()
        val btnPhone = promptView.findViewById<View>(R.id.nazovi)
        val btnEmail = promptView.findViewById<View>(R.id.email)

        btnPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + labelTel.text.toString())
            startActivity(intent)
            contactDialog.dismiss()
        }
        btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(model.getPonuda().email))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Upit za " + labelNaziv.text)
            if (intent.resolveActivity((activity as AppCompatActivity).packageManager) != null) {
                startActivity(intent)
            }
            contactDialog.dismiss()
        }
        contactDialog.setView(promptView)
        contactDialog.show()
    }

}