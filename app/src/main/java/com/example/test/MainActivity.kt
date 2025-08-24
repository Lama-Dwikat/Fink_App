package com.example.test

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test.ui.theme.TestViewModel
import com.example.test.ui.theme.type
import kotlin.text.toFloatOrNull
import kotlin.text.toIntOrNull

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val view: TestViewModel = viewModel()
            searchBar(view)
           // SkyMapScreen()


        }
    }
}



 @Composable
 fun searchBar(view:TestViewModel) {
     val searchedValue by view.searchedValue.collectAsState()
    val classesNames by view.classesNames.collectAsState()
    var expandClass by remember { mutableStateOf(false) }
    var expandHistory by remember { mutableStateOf(false) }
    var searchHistory = remember { mutableStateListOf<String>() }
     var showText by remember { mutableStateOf(false) }
    var searchClicked by remember { mutableStateOf(false) }
     val screenHeight = LocalConfiguration.current.screenHeightDp.dp
     val screenWidth = LocalConfiguration.current.screenWidthDp.dp
     var showImage by remember { mutableStateOf(true)}
     var iconStatus by remember { mutableStateOf(false)}


     val dynamicOffset = screenHeight *0.9f


    LaunchedEffect(Unit) {
        view.fetchClassesNames()
    }

    val allClasses = remember(classesNames) {
        listOf("Anomaly") + listOf("Unknown")+ listOfNotNull(
            classesNames.Fink,
            classesNames.TNS,
            classesNames.SIMBAD
        ).flatten().distinct()
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(top=screenHeight * 0.13f)

        ) {
            if(showImage) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.width(250.dp).height(180.dp)
                )
            }

            var paint = if (iconStatus)
                painterResource(R.drawable.image_icon)
                else painterResource(R.drawable.table_icon)

Row(horizontalArrangement = Arrangement.End ,  modifier=Modifier.fillMaxWidth()
) {
    Image(
        painter = paint,
        contentDescription = "Icon to select the result view",
        modifier = Modifier.clickable { iconStatus = !iconStatus }.size(45.dp)
    )
}
            Row (    verticalAlignment = Alignment.CenterVertically) {

                    Text(text = "Quick Fields : ", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    Text(
                        text = "Class ",
                        fontSize = 12.sp,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            expandClass = true; view.setSearchValue("class= ")
                        }
                    )
                Spacer(modifier = Modifier.size(3.dp))

                    Text(text = "Trend  ", fontSize = 12.sp, color = Color.Blue)
                    Spacer(modifier = Modifier.size(3.dp))

                    Text(text = "Last ", fontSize = 12.sp, color = Color.Blue)


                Spacer(modifier = Modifier.size(3.dp))

                    Text(text = "Radius ", fontSize = 12.sp, color = Color.Blue)
                    Spacer(modifier = Modifier.size(3.dp))

                    Text(text = "After ", fontSize = 12.sp, color = Color.Blue)
                    Spacer(modifier = Modifier.size(3.dp))

                    Text(text = "Before ", fontSize = 12.sp, color = Color.Blue)

                    Spacer(modifier = Modifier.size(3.dp))

                    Text(text = "Window ", fontSize = 12.sp, color = Color.Blue)


                }



            Spacer(modifier = Modifier.size(6.dp))



                TextField(
                    value = searchedValue,
                    onValueChange = { view.setSearchValue(it)  },
                    textStyle = TextStyle(
                        fontSize = 11.sp,
                        textAlign = TextAlign.Start, // or Center if you want center alignment
                        lineHeight = 15.sp
                    ),
                    placeholder = {
                        Text(
                            text = "Search, and you will find", fontSize = 13.sp,
                            textAlign = TextAlign.Start,
                        )
                    },
                    leadingIcon = {
                        //history icon

                        Text(
                            text = "\u23F1",
                            fontSize = 22.sp,
                              modifier = Modifier.clickable { expandHistory = true },
                            color = Color.Gray
                        )
                    },
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            modifier = Modifier.padding(end = 1.dp)
                        ) {
                            // For Empty  textField
                            Text(
                                text = "X",
                                color = Color.Gray,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clickable { view.setSearchValue("") }
                            )
                            //For doing the search
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(19.dp).clickable {
                                        if(searchedValue.isNotEmpty())
                                            if(searchHistory.contains(searchedValue)){
                                        searchHistory.remove(searchedValue)}
                                        searchHistory.add(0,searchedValue);
                                        searchClicked=true
                                        view.setSearchValue(searchedValue)
                                    }
                            )
                           //For search Documnetation


                            Text(
                                text = "?",
                                color = Color.Gray,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .padding(4.dp).clickable {
                                        showText=!showText
                                    }
                            )
                        }
                    },
                    modifier = Modifier.padding(3.dp).height(55.dp).width(350.dp)
                        .padding(bottom = 5.dp),
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                     unfocusedIndicatorColor = Color.Transparent,
                     disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White )



                )

  //drop down menu to show the list of classes
                if (expandClass) {
                    Box(modifier = Modifier.wrapContentSize((Alignment.TopStart)).padding(top = dynamicOffset,end=screenHeight*0.2f)) {
                    DropdownMenu(
                        expanded = expandClass,
                        onDismissRequest = { expandClass = false },
                        modifier = Modifier.height(250.dp).width(215.dp).background(Color.White)
                        )
                     {
                        allClasses.forEach { cls ->
                            DropdownMenuItem(
                                text = { Text("$cls") },
                                onClick = { view.setSearchValue("Class= $cls") ;expandClass=false})
                        }
                    }
                }
            }

            //drop down list for search history
            if (expandHistory){
                DropdownMenu(expanded = expandHistory, onDismissRequest = {expandHistory=false}, modifier = Modifier.heightIn(max=200.dp)
                    .background(Color.White))

       {
                    searchHistory.forEach { history ->
                    DropdownMenuItem(text = { Text("$history")}, onClick = {},)
                    }
                }

            }


            if (searchClicked){
                showImage=false
                view.processSearchValue()
                if(iconStatus)
                    secondResultView(view)
           else     firstResultView( view)
        }




            if (showText) {
Box(                     modifier = Modifier.border(60.dp, Color.Gray)){

                Popup(
                    alignment = Alignment.Center, // Center on the whole screen

                )
       {
           Box(modifier = Modifier.background(Color.White).height(400.dp).width(300.dp).verticalScroll(rememberScrollState()).padding(screenWidth*0.05f)
               , contentAlignment = Alignment.Center) {

               if( searchHelpText(view)){
               showText=false
            }

           }
       }
            }}






        }


            }


    }







@Composable
fun firstResultView(view: TestViewModel) {
    val obj by view.Objects.collectAsState()
    val cone by view.Conesearch.collectAsState()
    val typ by view.searchedType.collectAsState()

    val headers: List<String>
    val raws: List<List<String>>


    when (typ) {
        type.Coordinate -> {
            headers = listOf(
                "Object Id",
                "Separation",
                "Classification",
                "Number of Measurements",
                "Time Variation"
            )
            raws = cone.map { value ->
                listOf(
                    value.objectId,
                    "${value.separation}",
                    value.calssification,
                    "${value.numberOfMeasurments}",
                    "${value.timeVariation}"
                )
            }
        }

        type.Object, type.Class, type.Anomaly -> {
            headers = listOf(
                "Object Id",
                "Dec",
                "Ra",
                "Last Date",
                "Classification",
                "Number of Measurements",
                "Time Variation"
            )
            raws = obj.map { value ->
                listOf(
                    value.objectId,
                    "${value.dec}",
                    "${value.ra}",
                    value.lastDate,
                    value.classification,
                    "${value.numberOfMeasurments}",
                    "${value.timeVariation}"
                )
            }

        }
       else->{
           headers=emptyList()
           raws=emptyList()
       }
       }

  if(headers.isNotEmpty() && raws.isNotEmpty())
    table(headers,raws)

    }

@Composable
fun table(headers:List<String> , raws:List<List<String>>) {
    Box(
        modifier = Modifier.heightIn(max = 360.dp).width(330.dp).border(1.dp, Color(0xFFC9C9C9)) ,
            contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {

            Row {
                headers.forEach { title ->
                    Box(
                        modifier = Modifier.border(
                            1.dp, Color(0xFFC9C9C9)
                        ).width(135.dp).height(30.dp)
                            .background(Color(0xFFD3D3D3)), contentAlignment = Alignment.Center
                    ) {
                        Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Log.d("headers values",title)
                    }
                }


            }

            raws.forEach { raw ->
                Row {
                    raw.forEachIndexed { index, value ->
                        Box(
                            modifier = Modifier
                                //.border(1.dp, Color.Gray)
                                .width(135.dp).height(30.dp)
                                .background(Color.White)
                                .then (if (index==0) Modifier.clickable{} else Modifier)
                            , contentAlignment = Alignment.Center


                        ) {
                            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color= if (index==0) Color.Blue else Color.Black)
                        }
                    }


                }

            }

        }


    }
}








@Composable
fun searchHelpText(view:TestViewModel): Boolean {
    var value by remember {mutableStateOf("")}

        Column {
            Text("Search Help", fontSize = 15.sp, fontWeight = FontWeight.Bold)

            Text(
                "You may search for different kinds of data depending on what you enter. Below you will find the description of syntax rules and some examples.\n" +
                        "\n" +
                        "The search is defined by the set of search terms (object names or coordinates) and options (e.g. search radius, number of returned entries, etc). The latters may be entered either manually or by using the Quick fields above the search bar. Supported formats for the options are both name=value and name:value, where name is case-insensitive, and value may use quotes to represent multi-word sentences. For some of the options, interactive drop-down menu will be shown with possible values.\n" +
                        "\n" +
                        "The search bar has a button (on the left) to show you the list of your latest search queries so that you may re-use them, and a switch (on the right, right above the search field) to choose the format of results - either card-based (default, shows the previews of object latest cutout and light curve), or tabular (allows to see all the fields of objects"
            )
            Text("Search for specific ZTF objects", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                "To search for specific object, just use its name, or just a part of it. In the latter case, all objects with the names starting with this pattern will be returned.\n" +
                        "\n" + "Supported name patterns are as follows:"
            )
            Text("ZTFyyccccccc ")
            Text("for ZTF objects, i.e. ZTF followed with 2 digits for the year, and 7 characters after that")
            Text("TRCK_YYYYMMDD_HHMMSS_NN")
            Text("for tracklets detected at specific moment of time")
            Text("Examples:", fontWeight = FontWeight.Bold)
            Row {
                Text(
                    "ZTF21abfmbix ",
                    color = Color.Magenta,
                    modifier = Modifier.clickable { value="ZTF21abfmbix"})
                Text("- search for exact ZTF object")
            }

            Row {
                Text(
                    "- ZTF21abfmb",
                    color = Color.Magenta,
                    modifier = Modifier.clickable { value="ZTF21abfmb" })
                Text(" : search for partially matched ZTF object name", textAlign = TextAlign.Start)
            }
            Row {
                Text(
                    "- TRCK_20231213_133612_00",
                    color = Color.Magenta,
                    modifier = Modifier.clickable {value="TRCK_20231213_133612_00"})
                Text(
                    " : search for all objects associated with specific tracklet",
                    textAlign = TextAlign.Start
                )
            }
            Row {
                Text(
                    "- TRCK_20231213 ",
                    color = Color.Magenta,
                    modifier = Modifier.clickable { value = "TRCK_20231213"})
                Text(
                    ": search for all tracklet events from the night of Dec 13, 2023",
                    textAlign = TextAlign.Start
                )
            }
            Text(
                "Search around known astronomical objects",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "You can run a conesearch around a known astronomical name. Examples:\n" +
                        "\n" +
                        "Extended objects: M31\n" +
                        "Catalog names: TXS 0506+056\n" +
                        "TNS names: AT 2019qiz, SN 2024aaj\n" +
                        "By default, the conesearch radius is 10 arcseconds. You can change the radius by specifying r=<number> after the name, e.g. )"
            )
            Text(
                "Crab Nebula r=10m",
                color = Color.Magenta,
                modifier = Modifier.clickable { value= "Crab Nebula r=10m"})
            Text("(see the section Cone Search below)")
            Text("Cone search", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                "If you specify the position and search radius (using r option), all objects inside the given cone will be returned. Position may be specified by either coordinates, in either decimal or sexagesimal form, as exact ZTF object name, or as an object name resolvable through TNS or Simbad.\n" +
                        "\n" +
                        "The coordinates may be specified as:\n" +
                        "\n" +
                        "Pair of degrees\n"
            )
            Text(
                "HH MM SS.S [+-]?DD MM SS.S\n",
                color = Color.Magenta)
            Text(
                "HH:MM:SS.S [+-]?DD:MM:SS.S\n",
                color = Color.Magenta )
            Text(
                "HHhMMhSS.Ss [+-]?DDhMMhSS.Ss\n",
                color = Color.Magenta)
            Text(
                "optionally, you may use one more number as a radius, in either arcseconds, minutes (suffixed with m) or degrees (d). If specified, you do not need to provide the corresponding keyword (r) separately\n" +
                        "If the radius is not specified, but the coordinates or resolvable object names are given, the default search radius is 10 arcseconds.\n" +
                        "\n" +
                        "You may also restrict the alert variation time by specifying after and before keywords. They may be given as UTC timestamps in either ISO string format, as Julian date, or MJD. Alternatively, you may use window keyword to define the duration of time window in days."
            )
            Text("Examples:", fontWeight = FontWeight.Bold)
            Row {
                Text("246.0422 25.669 30",color = Color.Magenta ,modifier=Modifier.clickable { value="246.0422 25.669 30" })
                Text("- search within 30 arcseconds around RA=246.0422 deg Dec=25.669 deg")
            }
            Row {
                Text("246.0422 25.669 30 after=2023-03-29 13:36:52 window=10",color = Color.Magenta ,modifier=Modifier.clickable { value="246.0422 25.669 30 after=2023-03-29 13:36:52 window=10" })
                Text("- the same but also within 10 days since specified time moment")
            }
            Row {
                Text("16 24 10.12 +25 40 09.3",color = Color.Magenta ,modifier=Modifier.clickable { value="16 24 10.12 +25 40 09.3" })
                Text(" - search within 10 arcseconds around RA=10:22:31 Dec=+40:50:55.5")
            }
            Row {
                Text("Vega r=10m",color = Color.Magenta ,modifier=Modifier.clickable { value="Vega r=10m" })
                Text("- search within 600 arcseconds (10 arcminutes) from Vega")
            }
            Row {
                Text("ZTF21abfmbix r=20",color = Color.Magenta ,modifier=Modifier.clickable { value="ZTF21abfmbix r=20" })
                Text("- search within 20 arcseconds around the position of ZTF21abfmbix")}
                Row {
                    Text("AT2021co",color = Color.Magenta ,modifier=Modifier.clickable { value="AT2021co" })
                    Text("or")
                    Text("AT 2021co",color = Color.Magenta ,modifier=Modifier.clickable { value="AT 2021co" })
                    Text(" - search within 10 arcseconds around the position of AT2021co")}
                    Text("Solar System objects", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "To search for all ZTF objects associated with specific SSO, you may either directly specify sso keyword, which should be equal to contents of i:ssnamenr field of ZTF packets, or just enter the number or name of the SSO object that the system will try to resolve.\n" +
                                "\n" +
                                "So you may e.g. search for:\n" +
                                "\n" +
                                "Asteroids by proper name\n" +
                                "Vesta\n" +
                                "Asteroids by number"
                    )
                    Row {
                        Text("Asteroids (Main Belt):")
                        Text("8467 , ",color = Color.Magenta ,modifier=Modifier.clickable { value="8467" })
                        Text("1922 , ",color = Color.Magenta ,modifier=Modifier.clickable { value="1922" })
                        Text("33803 ",color = Color.Magenta ,modifier=Modifier.clickable { value="33803" })
                    }
                    Row {
                        Text("Asteroids (Hungarians):")
                        Text("18582 , ",color = Color.Magenta ,modifier=Modifier.clickable { value="18582" })
                        Text("77799 ",color = Color.Magenta ,modifier=Modifier.clickable { value="77799" })
                    }
                    Row {
                        Text("Asteroids (Jupiter Trojans):")
                        Text("4501 , ",color = Color.Magenta ,modifier=Modifier.clickable { value="4501" })
                        Text("1583 ",color = Color.Magenta ,modifier=Modifier.clickable { value="1583" })
                    }
                    Row {
                        Text("Asteroids (Mars Crossers):")
                        Text("302530",color = Color.Magenta ,modifier=Modifier.clickable { value="302530" })
                    }
                    Text("Asteroids by designation\n")
                    Row {
                        Text("2010JO69 , ",color = Color.Magenta ,modifier=Modifier.clickable { value="2010JO69" })
                        Text("2017AD19 , ",color = Color.Magenta ,modifier=Modifier.clickable { value="2017AD19" })
                        Text("2012XK111",color = Color.Magenta ,modifier=Modifier.clickable { value="2012XK111" })
                    }
                   Text( "Comets by number\n" )
                            Row {
                                Text("10P , ",color = Color.Magenta ,modifier=Modifier.clickable { value="10P" })
                                Text("249P , ",color = Color.Magenta ,modifier=Modifier.clickable { value="249P" })
                                Text("124P",color = Color.Magenta ,modifier=Modifier.clickable { value="124P" })
                            }
                    "Comets by designation\n" +
                            Row {
                                Text("C/2020V2 , ",color = Color.Magenta ,modifier=Modifier.clickable { value="C/2020V2" })
                                Text("C/2020R2",color = Color.Magenta ,modifier=Modifier.clickable { value="C/2020R2" })
                                Text(
                                    "Class-based search",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "To see the list of latest objects of specific class (as listed in v:classification alert field), just specify the class keyword. By default it will return 100 latest ones, but you may also directly specify last keywords to alter it.\n" +
                                            "\n" +
                                            "You may also specify the time interval to refine the search, using the self-explanatory keywords before and after. The limits may be specified with either time string, JD or MJD values. You may either set both limiting values, or just one of them. The results will be sorted in descending order by time, and limited to specified number of entries.\n" +
                                            "\n" +
                                            "Finally, you can specify a trend to your search, such as rising or fading. Use the keyword trend to see the list of available trends. This is an experimental feature that is expected to evolve."
                                )
                                Text("Examples:", fontWeight = FontWeight.Bold)
                                Row {
                                    Text("class= Unknown",color = Color.Magenta ,modifier=Modifier.clickable { value="class=Unknown" })
                                    Text("- return 100 latest objects with class Unknown")
                                }
                                Row {
                                    Text("last=10 class=\"Early SN Ia candidate",color = Color.Magenta ,modifier=Modifier.clickable { value="last=10 class=\"Early SN Ia candidate" })
                                    Text("- return 10 latest arly SN Ia candidates")
                                }
                                Row {
                                    Text("class=\"Early SN Ia candidate\" before=\"2023-12-01\" after=\"2023-11-07 04:00:00\"",color = Color.Magenta ,modifier=Modifier.clickable { value="class=\"Early SN Ia candidate\" before=\"2023-12-01\" after=\"2023-11-07 04:00:00\"" })
                                    Text("- objects of the same class between 4am on Nov 15, 2023 and Dec 1, 2023")
                                }
                                Row {
                                    Text("class=\"Early SN Ia candidate\" before=\"2023-12-01\" after=\"2023-11-07 04:00:00\" trend=rising",color = Color.Magenta ,modifier=Modifier.clickable { value="class=\"Early SN Ia candidate\" before=\"2023-12-01\" after=\"2023-11-07 04:00:00\" trend=rising" })
                                    Text("- objects of the same class between 4am on Nov 15, 2023 and Dec 1, 2023, that were rising (becoming brighter).")
                                }
                                Row {
                                    Text("class=\"(CTA) Blazar\" trend=low_state after=2025-02-01 before=2025-02-13",color = Color.Magenta ,modifier=Modifier.clickable { value="class=\"(CTA) Blazar\" trend=low_state after=2025-02-01 before=2025-02-13" })
                                    Text("- Blazars selected by CTA which were in a low state between the 1st February and 13th February 2025.")
                                }

                            }




                }

    if( value.isNotEmpty()){
        view.setSearchValue(value)
        Log.d("search value",value)
        return true
    }
    else return false
            }





@Composable
fun AladinLiteWebView() {
    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://aladin.cds.unistra.fr/AladinLite/")
        }
    })
}

























@Composable
fun SkyMapScreen() {
    val ra = 270.925
    val dec = -23.01
    val fov = 1.5

    val htmlContent = """
        <html>
          <head>
            <link rel="stylesheet" href="https://aladin.u-strasbg.fr/AladinLite/api/v2/latest/aladin.min.css"/>
            <script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>
            <script src="https://aladin.u-strasbg.fr/AladinLite/api/v2/latest/aladin.min.js"></script>
          </head>
          <body style="margin:0; padding:0;">
            <div id="aladin-lite-div" style="width:100%; height:100vh;"></div>
            <script>
              var aladin = A.aladin('#aladin-lite-div', {
                survey: "P/DSS2/color",
                fov: $fov,
                target: "$ra $dec"
              });
            </script>
          </body>
        </html>
    """.trimIndent()

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient() // Important!
                loadDataWithBaseURL(
                    "https://aladin.u-strasbg.fr/",
                    htmlContent,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        }
    )
}





@Composable
fun secondResultView( view: TestViewModel) {
    val cone by view.Conesearch.collectAsState()
    val obj by view.Objects.collectAsState()
    val typ by view.searchedType.collectAsState()
    val img by view.bitmap.collectAsState()


    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .heightIn(max = 300.dp)
            .verticalScroll(verticalScrollState)
            .background(Color.White)
            .border(1.dp, Color.LightGray)
    ) {
        obj.forEachIndexed { index, value ->

            var expanded by remember { mutableStateOf(false) }

            val objectId = value.objectId


            val points = obj.mapNotNull { valuesMap ->
                val diffMag = valuesMap.magnitudeDifference.toFloat()
                val calcJd = valuesMap.calculatedJd.toFloat()
                val fid = valuesMap.fid

                if (diffMag != null && calcJd != null && fid != null) {
                    Triple(calcJd, diffMag, fid)
                } else null
            }



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.padding(vertical = 8.dp)
                    .border(1.dp, Color.LightGray)
                    .padding(8.dp)
            ) {
                // Object ID
                // Object ID
                Text(
                    text = objectId,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Image on the left
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Gray)
                    ) {
                        img?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Cutout Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // ✅ Data on the right
                    Column(modifier = Modifier.weight(1f)) {
                        val measurements = value.numberOfMeasurments
                        val lapseRaw = value.timeVariation
                        val lapse = lapseRaw?.let { String.format("%.1f", it) } ?: lapseRaw

                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Magenta)) {
                                    append(measurements.toString())
                                }
                                append(" detection(s) in ")
                                withStyle(style = SpanStyle(color = Color.Magenta)) {
                                    append(lapse.toString())
                                }
                                append(" days")
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        // First alert
                        Row {
                            Text("First: ", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(value.firstDate, fontSize = 10.sp, color = Color.Magenta)
                        }

                        // Last alert
                        Row {
                            Text("Last: ", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(value.lastDate, fontSize = 10.sp, color = Color.Magenta)
                        }

                        // Equatorial coordinates
                        Row {
                            Text("Equ: ", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(value.equ, fontSize = 10.sp, color = Color.Magenta)
                        }

                        // Galactic coordinates
                        val galacticRaw = value.gal
                        val galacticFormatted = galacticRaw.toString()
                            .replace("(", "")
                            .replace(")", "")
                            .split(",")
                            .map { it.trim() }
                            .mapNotNull { part ->
                                part.toDoubleOrNull()?.let { String.format("%.4f", it) }
                            }
                            .take(2)
                            .joinToString(" ")

                        Row {
                            Text("Gal: ", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(
                                if (galacticFormatted.isNotEmpty()) galacticFormatted else galacticRaw.toString(),
                                fontSize = 10.sp,
                                color = Color.Magenta
                            )
                        }

                        // RealBogus score
                        Row {
                            Text("RealBogus: ", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(
                                value.RealBogus.toString(),
                                fontSize = 10.sp,
                                color = Color.Magenta
                            )
                        }
                    }
                }


                // ✅ Only toggle the plot
                Text(
                    text = if (expanded) "See Less" else "See More",
                    color = Color.Blue,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clickable { expanded = !expanded }
                )

                if (expanded) {
                    Spacer(modifier = Modifier.height(6.dp))

                    LightCurvePlot(
                        points = points,
                        modifier = Modifier.fillMaxWidth()
                    )


                }
            }
        }
    }


}
@Composable
fun LightCurvePlot(
    points: List<Triple<Float, Float, Int>>,  // x, y, fid
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) {
        Text("No light curve data available", modifier = modifier.padding(8.dp))
        return
    }

    val sortedPoints = points.sortedBy { it.first } // sort by JD (x-axis)

    val minX = sortedPoints.minOf { it.first }
    val maxX = sortedPoints.maxOf { it.first }
    val minY = sortedPoints.minOf { it.second }
    val maxY = sortedPoints.maxOf { it.second }

    Canvas(modifier = modifier
        .height(180.dp)
        .background(Color(0xFFD3D3D3))
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val leftPadding = 60f
        val bottomPadding = 30f
        val topPadding = 15f

        // Draw axes, gridlines (same as before)...

        // Draw points with color by fid
        sortedPoints.forEach { (xVal, yVal, fid) ->
            val x = leftPadding + (xVal - minX) / (maxX - minX) * (canvasWidth - leftPadding - 10f)
            val y = topPadding + (yVal - minY) / (maxY - minY) * (canvasHeight - topPadding - bottomPadding)

            val pointColor = when (fid) {
                1 -> Color(0xFF1D1B70) // blue
                2 -> Color(0xFFFF7F50) // orange
                else -> Color.Gray
            }

            drawCircle(
                color = pointColor,
                center = Offset(x, y),
                radius = 6f
            )
        }

        // Draw axis labels (same as before)...
    }
}





