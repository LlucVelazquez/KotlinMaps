package cat.itb.damv.m78

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.identity.util.UUID
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.*

data class MarkerData(
    val id: String = UUID.randomUUID().toString(),
    var latLng: LatLng,
    var title: String = "",
    var description: String = ""
)

class MarkersViewModel : ViewModel() {
    val markers = mutableStateListOf<MarkerData>()
    init {
        markers.addAll(
            listOf(
                MarkerData(latLng = LatLng(41.3851, 2.1734), title = "Barcelona"),
                MarkerData(latLng = LatLng(40.4168, -3.7038), title = "Madrid"),
                MarkerData(latLng = LatLng(48.8566, 2.3522), title = "París"),
                MarkerData(latLng = LatLng(41.45417990295869, 2.1856497659642167), title = "ITB")
            )
        )
    }

    fun removeMarker(marker: MarkerData) {
        markers.remove(marker)
    }

    fun updateMarker(updated: MarkerData) {
        val index = markers.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            markers[index] = updated
        }
    }

    fun getMarkerById(id: String): MarkerData? =
        markers.find { it.id == id }
}



@SuppressLint("UnrememberedMutableState")
@Composable
fun MapsScreen(viewModel: MarkersViewModel = viewModel()){

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.45417990295869, 2.1856497659642167), 5f)
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(

            googleMapOptionsFactory = {
                GoogleMapOptions().mapId("DEMO_MAP_ID")
            },
            onMapLongClick = { latLng ->
                val newMarker = MarkerData(latLng = latLng)
                viewModel.markers.add(newMarker)
            },
            cameraPositionState = cameraPositionState,

        ) {
            Circle(
                center = LatLng(41.45417990295869, 2.1856497659642167),
                radius = 100.0,
                strokeColor = Color.Blue,
                fillColor = Color.Transparent,
            )
            viewModel.markers.forEach { markerData ->
                AdvancedMarker(
                    state = MarkerState(position = markerData.latLng),
                    title = markerData.title
                )
            }

        }

        Button(
            onClick = {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(41.45417990295869, 2.1856497659642167), 5f)
            }
        ) {
            Text("Tornar")
        }
    }
}
