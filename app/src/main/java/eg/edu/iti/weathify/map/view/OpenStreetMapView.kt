package eg.edu.iti.weathify.map.view

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(lat: Double, lon: Double, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    AndroidView(factory = { mapView }, modifier = modifier) { map ->
        val mapController = map.controller
        val newLocation = GeoPoint(lat, lon)

        mapController.setZoom(10.0)
        mapController.setCenter(newLocation)

        map.overlays.clear()
        val marker = Marker(map).apply {
            position = newLocation
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        map.overlays.add(marker)

        map.invalidate()
        map.postInvalidate()
    }
}
