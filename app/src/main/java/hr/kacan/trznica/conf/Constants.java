package hr.kacan.trznica.conf;

import java.util.ArrayList;
import java.util.List;

import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.models.TipProizvoda;

/**
 * Klasa koja sadrži konstante
 *
 * @author Kristijan Kačan
 * @since prosinac, 2019.
 */
public class Constants {

    public static final String BASE_URL = "http://ec2-18-185-93-51.eu-central-1.compute.amazonaws.com/";
    public static final String API_BASE_URL = "http://ec2-18-185-93-51.eu-central-1.compute.amazonaws.com/api/v1/";//PROMINITI U MAIN.PY APP.RUN HOST!!!
    //public static final String API_BASE_URL = "http://192.168.1.30:5000/api/v1/"; //PROMINITI U MAIN.PY APP.RUN HOST!!!
    public static final String IMAGE_PREFIX = BASE_URL+ "images/";
    public static List<TipProizvoda> TIP_PROIZVODA_LIST = new ArrayList<>();
    public static Korisnik KORISNIK;
    public static final String RESPONSE_SUCCESS ="Success";
    public static final String RESPONSE_EXIST ="Exist";
    public static final String RESPONSE_FAIL ="Fail";
    public static long TIP_PROIZVODA_ID = 0;
    public static String TIP_PROIZVODA_NAZIV = "";


}
