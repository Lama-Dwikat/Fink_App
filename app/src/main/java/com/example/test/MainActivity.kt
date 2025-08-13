package com.example.test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test.ui.theme.TestViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val view: TestViewModel = viewModel()

            searchBar(view)



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
    var searching by remember { mutableStateOf(false) }
     val screenHeight = LocalConfiguration.current.screenHeightDp.dp
     val screenWidth = LocalConfiguration.current.screenWidthDp.dp

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
            modifier = Modifier.padding(top=screenHeight * 0.185f)

                .verticalScroll(rememberScrollState())

        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.width(220.dp).height(150.dp)
            )


            Row {
                Text(text = "Quick Fields : ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Class ",
                    fontSize = 11.sp,
                    color = Color.Blue,
                    modifier = Modifier.clickable { expandClass = true ;  view.setSearchValue("class= ")
                    }
                )
                Text(text = "Trend  ", fontSize = 11.sp, color = Color.Blue)
                Text(text = "Last ", fontSize = 11.sp, color = Color.Blue)
                Text(text = "Radius ", fontSize = 11.sp, color = Color.Blue)
                Text(text = "After ", fontSize = 11.sp, color = Color.Blue)
                Text(text = "Before ", fontSize = 11.sp, color = Color.Blue)
                Text(text = "Window ", fontSize = 11.sp, color = Color.Blue)
            }

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
                                        searching=true
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


            if (searching){
                view.processSearchValue()
                searchProcess( view)
        }




            if (showText) {
Box(                     modifier = Modifier.border(60.dp, Color.Gray)){

                Popup(
                    alignment = Alignment.Center, // Center on the whole screen

                )
       {
           Box(modifier = Modifier.background(Color.White).height(400.dp).width(300.dp).verticalScroll(rememberScrollState()).padding(screenWidth*0.05f)



               , contentAlignment = Alignment.Center) {
            if( searchHelpText().isNotEmpty()){
                view.setSearchValue(searchHelpText())
                showText=false
            }
//               , modifier = Modifier.fillMaxSize(), // fill space so alignment can take effect
//                   textAlign = TextAlign.Start)
           }
       }
            }}











        }







            }


    }




  @Composable
  fun searchProcess(view:TestViewModel) {
      val cone by view.Conesearch.collectAsState()

      Text("Cone search with center at 246.0422 25.669 and radius 30.0 arcsec - 1 objects found")
      Box(
          modifier = Modifier.heightIn(max = 320.dp).width(330.dp).border(2.5.dp, Color.Gray),
          contentAlignment = Alignment.TopCenter
      ) {
          Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {

              Row {
                  listOf(
                      "Object Id",
                      "Separation",
                      "Classification",
                      "Number of Measurements",
                      "Time Variation"
                  ).forEach { title ->
                      Box(
                          modifier = Modifier.border(
                              1.dp, Color.Gray
                          ).width(135.dp).height(30.dp)
                              .background(Color(0xFFBFBFBF)), contentAlignment = Alignment.Center
                      ) {
                          Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                      }
                  }


              }
              Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                  cone.forEach { co ->
                      Row {
                          listOf(
                              co.objectId,
                              co.separation.toString(),
                              co.calssification,
                              co.numberOfMeasurments.toString(),
                              co.timeVariation
                          ).forEach { title ->
                              Box(
                                  modifier = Modifier.border(1.dp, Color.Gray).width(135.dp)
                                      .height(30.dp)
                                      .background(Color.White), contentAlignment = Alignment.Center
                              ) {
                                  Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                              }
                          }


                      }
                  }
              }
          }
      }

  }



@Composable
fun searchHelpText():String {
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
                    modifier = Modifier.clickable { })
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
                Text("246.0422 25.669 30 after=\"2023-03-29 13:36:52\" window=10",color = Color.Magenta ,modifier=Modifier.clickable { value="246.0422 25.669 30 after=\"2023-03-29 13:36:52\" window=10" })
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
                    "Comets by number\n" +
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
    return value
            }


