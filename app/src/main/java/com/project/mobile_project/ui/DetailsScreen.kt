package com.project.mobile_project.ui


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.project.mobile_project.R
import com.project.mobile_project.data.Activity
import com.project.mobile_project.viewModel.ActivitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(activitiesViewModel: ActivitiesViewModel) {
    val context = LocalContext.current
    val selectedActivity = activitiesViewModel.activitySelected
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = { shareDetails(context, selectedActivity) }) {
                Icon(Icons.Filled.Share, contentDescription = stringResource(id = R.string.share_activity))
            }
        }
            ){ paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = selectedActivity?.name?:stringResource(id = R.string.activity_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.size(15.dp))

            if (selectedActivity?.description?.isNotEmpty() == true) {
                Text(
                    text = selectedActivity?.description
                        ?: stringResource(id = R.string.activity_description),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.size(15.dp))
            }
        }
    }
}

private fun shareDetails(context: Context, activity: Activity?){
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, activity?.name?:"No activity")
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}