package com.example.test.ui.theme
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer

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
    val timeVariation:Double

)

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
    val startDate:Double
){
    val timeVariation:String
        get()=(jd-startDate).toString()
}

data class classRequest(
    @SerializedName("class")
    val className:String
)

data class anomalyRequest(

    val n:Int=10           //n=number of alerts
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