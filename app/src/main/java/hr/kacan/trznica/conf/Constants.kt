package hr.kacan.trznica.conf

/**
 * Klasa konstanti
 *
 * @author Kristijan Kačan
 * @since prosinac, 2020.
 */
object Constants {
    private const val BASE_URL: String = "http://ec2-18-185-93-51.eu-central-1.compute.amazonaws.com/"
    const val API_BASE_URL: String = "http://ec2-18-185-93-51.eu-central-1.compute.amazonaws.com/api/v1/"
    //const val BASE_URL: String = "http://192.168.1.100:5000/"
    //const val API_BASE_URL: String =  "http://192.168.1.100:5000/api/v1/"
    const val IMAGE_PREFIX: String = BASE_URL + "images/"
    const val SVI_PROIZVODI_NAME: String = "Svi proizvodi"
    const val SVI_PROIZVODI_IMAGE: String = "http://ec2-18-185-93-51.eu-central-1.compute.amazonaws.com/images/11.png"
    const val RESPONSE_SUCCESS: String = "Success"
    const val RESPONSE_EXIST: String = "Exist"
    const val RESPONSE_FAIL: String = "Fail"
    const val RESPONSE_FAIL_PWD: String = "Fail Password"
    const val RESPONSE_FAIL_USN: String = "Fail Username"

}