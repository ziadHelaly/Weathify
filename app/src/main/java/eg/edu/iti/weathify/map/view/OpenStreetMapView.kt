package eg.edu.iti.weathify.map.view

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(lat: Double, lon: Double, modifier: Modifier = Modifier,updateSelectedLocation:(Double,Double)->Unit) {
    val context = LocalContext.current

    // Load the OsmDroid configuration
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    }

    // Always track the latest location
    val geoPoint = remember(lat, lon) { GeoPoint(lat, lon) }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    AndroidView(factory = { mapView }, modifier = modifier.fillMaxSize()) { map ->
        val mapController = map.controller
        mapController.setZoom(10.0)
        mapController.setCenter(geoPoint)

        // Function to update marker
        fun updateMarker(location: GeoPoint) {
            map.overlays.clear()
            val marker = Marker(map).apply {
                position = location
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            map.overlays.add(marker)
            mapController.animateTo(location) // Smooth movement
            map.invalidate()
        }

        // Update when lat/lon change
        updateMarker(geoPoint)

        // Add tap listener using MapEventsOverlay
        val overlayEvents = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    updateMarker(it)
                    updateSelectedLocation(it.longitude,it.latitude)

                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val eventsOverlay = MapEventsOverlay(overlayEvents)
        map.overlays.add(eventsOverlay)
    }
}
