package com.project.mobile_project.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.project.mobile_project.R
import com.project.mobile_project.data.User
import com.project.mobile_project.utilities.createImageFile
import com.project.mobile_project.utilities.saveImage
import com.project.mobile_project.viewModel.UsersViewModel
import java.util.*

@Composable
fun ProfileScreen(usersViewModel: UsersViewModel) {

    val context = LocalContext.current
    var photoURI by rememberSaveable { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_shared_preferences), Context.MODE_PRIVATE)
    val user = getUser(usersViewModel.allUsers.collectAsState(initial = listOf()).value, sharedPreferences.getString(context.getString(R.string.username_shared_pref), ""))
    Log.d("DEBUG", user.toString())
    val imageModifier = Modifier
        .size(200.dp)
        .border(
            BorderStroke(2.dp, Color.Black),
            CircleShape
        )
        .clip(CircleShape)

    Column (
        modifier = Modifier
            .padding(all = 12.dp)
            .fillMaxSize(),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (user?.username?.isNotEmpty() == true) user.username else "Username",
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.size(15.dp))

        val context = LocalContext.current
        val file = context.createImageFile()
        val uri = FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            context.packageName + ".provider", file
        )

        var capturedImageUri by remember {
            mutableStateOf<Uri>(Uri.EMPTY)
        }

        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    capturedImageUri = uri
                }
            }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        if (capturedImageUri.path?.isNotEmpty() == true) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(capturedImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "image taken",
                modifier = imageModifier,
                contentScale = ContentScale.Crop,
            )
            user?.username?.let { usersViewModel.updateProfileImg(it, saveImage(context.applicationContext.contentResolver, capturedImageUri)) }
        } else if (user?.profileImg?.isEmpty() == null /*true*/) {
            Image(
                painter = painterResource(id = R.drawable.baseline_android_24),
                contentDescription = "image placeholder",
                modifier = imageModifier.background(Color.LightGray),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Uri.parse(user.profileImg))
                    .crossfade(true)
                    .build(),
                contentDescription = "profile img",
                modifier = imageModifier
            )
        }

        Spacer(modifier = Modifier.size(15.dp))

        Button(
            onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
        ) {
            Icon(
                Icons.Filled.PhotoCamera,
                contentDescription = "Camera icon",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Take a picture")
        }

        Spacer(modifier = Modifier.size(15.dp))


        Text(
            text = if (user?.firstName?.isNotEmpty() == true
                && user?.lastName?.isNotEmpty() == true) {
                user.firstName + " " + user.lastName
            } else "Nome Cognome",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.size(15.dp))

        Text(
            text = if (user?.email?.isNotEmpty() == true) user.email else "trembolone.legal@ronnie.com",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun getUser(users: List<User>, username: String?): User? {
    val optionalUser = users.stream().filter {x -> x.username == username}.findFirst()
    return if(optionalUser.isPresent) {
        optionalUser.get()
    } else {
        null
    }
}
