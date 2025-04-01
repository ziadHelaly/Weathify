package eg.edu.iti.weathify.map.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.map.viewModel.MapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel, modifier: Modifier = Modifier, onNavBack: () -> Unit) {
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()

    var query by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        selectedLocation.let { (lat, lon) ->
            OpenStreetMapView(lat, lon, Modifier.fillMaxSize()) { longitude, latitude ->

                viewModel.selectLocation(
                    lat = latitude,
                    lon = longitude,
                    name = "usa"
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            SearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    viewModel.updateSearchQuery(it)
                    isSearching = it.isNotEmpty()
                },
                onSearch = { isSearching = false },
                active = isSearching,
                onActiveChange = { isSearching = it },
                placeholder = { Text(stringResource(R.string.search_city)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSearching && searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(searchResults) { city ->
                            SearchItem(city.display_name) {
                                viewModel.selectLocation(
                                    city.lat.toDouble(),
                                    city.lon.toDouble(),
                                    city.display_name
                                )
                                query = ""
                                isSearching = false
                            }
                        }
                    }
                }

            }

        }
        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = {
                selectedLocation.let { (lat, lon, name) ->
                    viewModel.addToFav(lat, lon, name)
                    onNavBack()
                }
            }) {
            Text(stringResource(R.string.add_to_fav))
        }
    }
}

@Composable
fun SearchItem(cityTitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            painterResource(R.drawable.ic_location),
            "",
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Text(
            text = cityTitle,
            fontSize = 16.sp,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge,
        )

    }
}
