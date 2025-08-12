package com.example.test.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


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


    private val _classes = MutableStateFlow<List<objectResponse>>(emptyList())
    val classes:MutableStateFlow<List<objectResponse>> = _classes

    fun fetchClasses(className:String) {
        viewModelScope.launch {
            try {
                val request = classRequest(className)
                _classes.value = RetrofitObject.api.postClasses(request)
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
    private val _anomalyClass=MutableStateFlow<List<objectResponse>>(emptyList())
    val anomalyClass:MutableStateFlow<List<objectResponse>> = _anomalyClass
    fun fetchAnomaly(){
        viewModelScope.launch{
            try{
                _anomalyClass.value=RetrofitObject.api.postAnomaly(anomalyRequest())

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

    fun processSearchValue(){
        viewModelScope.launch {
            try{
                if(_searchedValue.value.isNotEmpty()) {
                    var values = _searchedValue.value.trim().split(" ")
                    when {
                        values.first().all { it.isDigit() } && values.size >= 2 -> {
                      fetchConeSearch( values[0].toDouble(),values[1].toDouble(),values[2].toDouble())

                        }
           values.size>=2 && values[0].equals("class=")-> {
               fetchClasses(values[1])
           }
                  //      values.size>=1 && values.
                else-> fetchObject(values[0])
                    }//when end

                }// if end
            }catch(e:Exception){
                println("Error while proccesing search value")
            }
        }



    }//function end


} //class end





