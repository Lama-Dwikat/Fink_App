package com.example.test.ui.theme
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class objectRequest(
    val objectId:String
    )

//it used for objects and classes
data class objectResponse(
    @SerializedName("i:objectId")
    val objectId:String,

    @SerializedName("i:ra")
    val ra:Double,

    @SerializedName("i:dec")
    val dec:Double,

    @SerializedName("v:classification")
    val classification:String,

    @SerializedName("v:lastdate")
    val lastDate:String,

    @SerializedName("d:nalerthist")
    val numberOfMeasurments: Int,

    @SerializedName("v:lapse")
    val timeVariation:Double,
    @SerializedName ("v:firstdate")
    val firstDate:String,

    @SerializedName("i:rb")
    val RealBogus:Double,

    @SerializedName("i:jd")
    val jd:Double,

    @SerializedName("i:fid")
    val fid:Int,

    @SerializedName("i:magpsf")
    val magnitudeDifference:Double,

    @SerializedName("i:distnr")
    val ztf:Double,

     @SerializedName("i:distpsnr1")
    val psi:Double,

    @SerializedName("i:neargaia")
    val gaia:Double

)
{
    val equ: String get() = getEquString(ra, dec)
    val gal: Pair<Double, Double> get() = convertRaDecToGal(ra, dec)
    val calculatedJd: Double get() = calculateJD(lastDate)
}




data class conesearchRequest(
    val ra:Double,

    val dec:Double,

    val radius:Double

)

data class conesearchResponse(
    @SerializedName("i:ra")
    val ra:Double,

    @SerializedName("i:dec")
    val dec:Double,

    @SerializedName("i:raduis")
    val radius:Double,

    @SerializedName("i:objectId")
    val objectId:String,

    @SerializedName("d:classification")
    val calssification:String,

    @SerializedName("v:separation_degree")
    val separation:Double,

    @SerializedName("d:nalerthist")
    val numberOfMeasurments:Int,

    @SerializedName("i:jd")
    val jd:Double,

@SerializedName("i:jdstarthist")
    val firstDate:Double
){
    val timeVariation:String
        get()=(jd-firstDate).toString()

    val equ: String get() = getEquString(ra, dec)
    val gal: Pair<Double, Double> get() = convertRaDecToGal(ra, dec)
}










data class classRequest(
    @SerializedName("class")
    val className:String
)

data class anomalyRequest(

    val n:Int=100          //n=number of alerts
)

data class imageRequest(
    val objectId:String,
    val kind:String="Science"
)

data class classNameResponse(
    @SerializedName("Cross-match with SIMBAD (see http://simbad.u-strasbg.fr/simbad/sim-display?data=otypes)")
    val SIMBAD:List<String>,

    @SerializedName("Cross-match with TNS")
    val TNS:List<String>,

    @SerializedName("Fink classifiers")
    val Fink:List<String>

)





fun convertRaDecToGal(raDeg: Double, decDeg: Double): Pair<Double, Double> {
    // Convert RA/Dec to radians
    val raRad = Math.toRadians(raDeg)
    val decRad = Math.toRadians(decDeg)

    // Convert to cartesian unit vector
    val x = cos(decRad) * cos(raRad)
    val y = cos(decRad) * sin(raRad)
    val z = sin(decRad)

    // Rotation matrix (ICRS to Galactic)
    val r = arrayOf(
        doubleArrayOf(-0.0548755604, -0.8734370902, -0.4838350155),
        doubleArrayOf( 0.4941094279, -0.4448296300,  0.7469822445),
        doubleArrayOf(-0.8676661490, -0.1980763734,  0.4559837762)
    )

    // Apply rotation
    val xg = r[0][0] * x + r[0][1] * y + r[0][2] * z
    val yg = r[1][0] * x + r[1][1] * y + r[1][2] * z
    val zg = r[2][0] * x + r[2][1] * y + r[2][2] * z

    // Convert back to spherical
    val lRad = atan2(yg, xg)
    val bRad = asin(zg)

    // Convert to degrees and normalize
    var lDeg = Math.toDegrees(lRad)
    if (lDeg < 0) lDeg += 360.0
    val bDeg = Math.toDegrees(bRad)

    return Pair(lDeg, bDeg)
}




fun raDecToHMS(ra: Double): String {
    val totalSeconds = ra * 3600 / 15
    val hours = (totalSeconds / 3600).toInt()
    val minutes = ((totalSeconds % 3600) / 60).toInt()
    val seconds = totalSeconds % 60
    return String.format("%02d %02d %.2f", hours, minutes, seconds)
}

fun decToDMS(dec: Double): String {
    val sign = if (dec >= 0) "+" else "-"
    val absDec = Math.abs(dec)
    val degrees = absDec.toInt()
    val minutes = ((absDec - degrees) * 60).toInt()
    val seconds = ((absDec - degrees - minutes / 60.0) * 3600)
    return String.format("$sign%02d %02d %.1f", degrees, minutes, seconds)
}

fun getEquString(ra: Double, dec: Double): String {
    return "${raDecToHMS(ra)} ${decToDMS(dec)}"
}




fun calculateJD(dateString: String): Double {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("UTC")

    val date = formatter.parse(dateString) ?: throw IllegalArgumentException("Invalid date format: $dateString")
    val epochMillis = date.time

    // Convert milliseconds since epoch to Julian Date
    return epochMillis / 86400000.0 + 2440587.5
}



