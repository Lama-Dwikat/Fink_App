package com.example.test.ui.theme

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.asinh
import kotlin.math.pow
import kotlin.math.roundToInt

enum class type{ Coordinate,Object,Class,Anomaly ,None}

class TestViewModel: ViewModel() {


    private val _Objects=MutableStateFlow<List<objectResponse>>(emptyList())
      val Objects:StateFlow<List<objectResponse>> = _Objects

    fun fetchObject(objectId:String){
        viewModelScope.launch {

        try {
            val request = objectRequest(objectId)
            _Objects.value = RetrofitObject.api.postObject(request)
        }
        catch (e:Exception){
        println("Error while fetching objects")}
        }

    }


    private val _Conesearch= MutableStateFlow <List<conesearchResponse>>(emptyList())
    val Conesearch: StateFlow<List<conesearchResponse>> = _Conesearch


    fun fetchConeSearch(ra:Double,dec:Double,radius:Double){

        viewModelScope.launch {
            try{
                val request=conesearchRequest(ra,dec,radius)
                _Conesearch.value= RetrofitObject.api.postConesearch(request)
            }
            catch(e:Exception){
                println("Error while fetching conesearch")
            }
        }
    }




    fun fetchClasses(className:String) {
        viewModelScope.launch {
            try {
                val request = classRequest(className)
                Log.d("before fetch the class",request.toString())

                _Objects.value = RetrofitObject.api.postClasses(request)


            }
            catch(e:Exception){
                println("Error while fetching classes")
            }
        }
    }


    private val _classesNames = MutableStateFlow(classNameResponse(TNS = emptyList(), SIMBAD = emptyList(), Fink = emptyList()))
    val classesNames:MutableStateFlow<classNameResponse> = _classesNames
    fun fetchClassesNames(){
        viewModelScope.launch{
            try{
               _classesNames.value=RetrofitObject.api.getClassesNames()

            }
            catch(e:Exception){
                println("Error while fetching classes names")
            }
        }
    }

    fun fetchAnomaly(){
        viewModelScope.launch{
            try{
                Log.d("Anomaly Request", anomalyRequest().toString())
                _Objects.value=RetrofitObject.api.postAnomaly(anomalyRequest())


            }
            catch(e:Exception){
                println("Error while fetching Anomaly")
            }
        }
    }

    private val _searchedValue=MutableStateFlow("")
    val searchedValue:MutableStateFlow<String> =_searchedValue

    fun setSearchValue(newValue:String){
        viewModelScope.launch {
            try{
             _searchedValue.value=newValue
            }
            catch(e:Exception){
          println("Error while setting search value")
            }
        }

    }

   private val _searchedType = MutableStateFlow<type>(type.None)
    val searchedType : MutableStateFlow<type> = _searchedType


    fun processSearchValue(){
        viewModelScope.launch {
            try{
                if(_searchedValue.value.isNotEmpty()) {
                    var values = _searchedValue.value.trim().split(" ")
                    when {
                        values[0].first().isDigit() && values.size >= 2 -> {
                      fetchConeSearch( values[0].toDouble(),values[1].toDouble(),values[2].toDouble())
                            _searchedType.value=type.Coordinate

                        }
           values.size>=2 && values[0].equals("Class=")-> {
               if (values[1].equals("Anomaly")){
                   fetchAnomaly()
                   _searchedType.value=type.Anomaly
                   fetchImage(values[1],"Science")
               }
              else{ fetchClasses(values[1].toString())
                   _searchedType.value=type.Class
              }
           }
                  //      values.size>=1 && values.
                else-> {
                    fetchObject(values[0])
                    fetchImage(values[0],"Science")
                    _searchedType.value= type.Object
                }
                    }//when end

                }// if end
            }catch(e:Exception){
                println("Error while proccesing search value")
            }
        }



    }//function end


    private val _bitmap= MutableStateFlow<Bitmap?>(null)
    val bitmap: MutableStateFlow<Bitmap?> = _bitmap
    fun fetchImage(objectId:String,type:String){
        viewModelScope.launch {
            try{
                //fetching image process
                val request= imageRequest(objectId,type)
           val response= RetrofitObject.api.postImage(request)
                val inputStream=response.byteStream()
                val originalBitmap = BitmapFactory.decodeStream(inputStream)

                val smoothedBitmap = smoothBitmap(originalBitmap)
                val asinhBitmap = applyAsinhFilter(smoothedBitmap)

                val blueDarkBitmap =    mapGrayscaleToBlueGradient(asinhBitmap)
                _bitmap.value = blueDarkBitmap


                //val input=re
        }catch (e:Exception){
        println("Error while trying to fetch the image ")}
        }



    }// end function fetch image

    fun applyAsinhFilter(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val scale = 0.01f

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)

                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)

                val nr = normalizeAsinh(r, scale)
                val ng = normalizeAsinh(g, scale)
                val nb = normalizeAsinh(b, scale)

                val newPixel = Color.argb(255, nr, ng, nb)
                result.setPixel(x, y, newPixel)
            }
        }
        return result
    }

    fun normalizeAsinh(value: Int, scale: Float): Int {
        val asinhValue = asinh(value * scale)
        val maxAsinh = asinh(255 * scale)
        return ((asinhValue / maxAsinh) * 255).roundToInt().coerceIn(0, 255)
    }

    fun mapGrayscaleToBlueGradient(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)

                // Standard grayscale brightness
                val brightness = (0.300 * r + 0.300 * g + 0.360 * b).toInt().coerceIn(0, 255)

                // Normalize brightness to 0.0 - 1.0
                val normalized = brightness / 255.0

                // Create blue gradient: dark blue to white
                val red = (normalized.pow(3.0) * 255).toInt().coerceIn(0, 255)
                val green = (normalized.pow(2.0) * 255).toInt().coerceIn(0, 255)
                val blue = (normalized .pow(0.7) * 255).toInt().coerceIn(100, 255)  // Always keep some blue

                result.setPixel(x, y, Color.rgb(red, green, blue))
            }
        }

        return result
    }



    fun smoothBitmap(bitmap: Bitmap): Bitmap {
        val kernel = arrayOf(
            floatArrayOf(1f, 2f, 1f),
            floatArrayOf(2f, 4f, 2f),
            floatArrayOf(1f, 2f, 1f)
        )

        val width = bitmap.width
        val height = bitmap.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val kSize = kernel.size
        val kCenter = kSize / 2

        for (y in 0 until height) {
            for (x in 0 until width) {
                var rSum = 0f
                var gSum = 0f
                var bSum = 0f
                var weightSum = 0f

                for (ky in 0 until kSize) {
                    for (kx in 0 until kSize) {
                        val px = x + kx - kCenter
                        val py = y + ky - kCenter

                        if (px in 0 until width && py in 0 until height) {
                            val pixel = bitmap.getPixel(px, py)
                            val weight = kernel[ky][kx]

                            rSum += Color.red(pixel) * weight
                            gSum += Color.green(pixel) * weight
                            bSum += Color.blue(pixel) * weight
                            weightSum += weight
                        }
                    }
                }

                val r = (rSum / weightSum).toInt().coerceIn(0, 255)
                val g = (gSum / weightSum).toInt().coerceIn(0, 255)
                val b = (bSum / weightSum).toInt().coerceIn(0, 255)

                result.setPixel(x, y, Color.argb(255, r, g, b))
            }
        }
        return result
    }















} //class end





