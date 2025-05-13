package cat.itb.damv.m78

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

// camera
@Composable
fun CameraScreen() {
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

}