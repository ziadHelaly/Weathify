package eg.edu.iti.weathify.favourite.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.core.view.components.FAB
import eg.edu.iti.weathify.favourite.viewModel.FavouriteViewModel

@Composable
fun FavouriteScreen(viewModel: FavouriteViewModel,navToHome:(String,String)->Unit ,navToMap: () -> Unit) {
    val favs by viewModel.allFavourites.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            FAB {
                navToMap()
            }
        }, modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                stringResource(R.string.favourites_cities),
                style = MaterialTheme.typography.titleLarge
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(favs) { fav ->
                    FavouriteItem(
                        name = fav.name,
                        onDisplayClick = {
                            navToHome(fav.longitude,fav.latitude)
                        }) {
                        viewModel.removeFromFavs(fav)
                    }
                }
            }
        }
    }
}

@Composable
private fun FavouriteItem(
    modifier: Modifier = Modifier,
    name: String,
    onDisplayClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDisplayClick() }
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp).weight(0.8f)
            )
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                "",
                modifier = Modifier.clickable { onClick() }.padding(8.dp).weight(0.2f))
        }

    }
}