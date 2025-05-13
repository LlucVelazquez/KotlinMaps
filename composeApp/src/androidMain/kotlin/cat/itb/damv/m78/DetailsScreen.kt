package cat.itb.damv.m78

import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun MarkerDetailScreen(
    markerId: String,
    navViewModel: NavViewModel,
    markersViewModel: MarkersViewModel = viewModel(),
    viewModel: Any = viewModel { CameraViewModel() }

) {
    val marker = markersViewModel.getMarkerById(markerId) ?: return
    var title by remember { mutableStateOf(marker.title) }
    var description by remember { mutableStateOf(marker.description) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Editar marcador", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Títol") }
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripció") }
        )
        Spacer(Modifier.height(20.dp))
        FeatureThatRequiresCameraPermission() {
            val viewModel = viewModel { CameraViewModel() }
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            LaunchedEffect(lifecycleOwner) {
                viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
            }
            Box {
                val photoUrl = viewModel.photoUrl.value
                if (photoUrl == null) {
                    val surfaceRequest = viewModel.surferRequest.value
                    surfaceRequest?.let { request ->
                        CameraXViewfinder(
                            surfaceRequest = request,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        {viewModel.takePhoto(context) },
                        modifier = Modifier.align(Alignment.Center)
                    ) { Text("Take Photo") }
                } else {
                    AsyncImage(model = photoUrl, "")
                }
            }

        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            markersViewModel.updateMarker(marker.copy(title = title, description = description))
            navViewModel.navTo(Screen.Markers)
        }) {
            Text("Desar")
        }
    }
}