package com.project.mobile_project.ui


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            var activityName by rememberSaveable { mutableStateOf(if(selectedActivity?.name?.isEmpty() == true) "" else selectedActivity?.name) }

                TextField(
                    value = activityName!!,
                    onValueChange = { newText: String -> activityName = newText },
                    label = { "Titolo" },
                    placeholder = { if (activityName == null) "Titolo" else  selectedActivity?.name}
                )

            Spacer(modifier = Modifier.size(15.dp))

            if (selectedActivity?.description?.isNotEmpty() == true) {
                Text(
                    text = selectedActivity?.description
                        ?: stringResource(id = R.string.activity_description),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.size(15.dp))
            }

            Text(
                text = "Tempo totale: " + selectedActivity?.totalTime + " secondi",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )

                Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "Distanza: " + selectedActivity?.distance + " metri",
                    //?: stringResource(id = R.string.distance_label),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "Velocità: " + selectedActivity?.speed + " m/s",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.size(15.dp))

            Button(
                //modifier = Modifier.align(alignment = Alignment.CenterVertically),
                onClick = {
                    activityName?.let {
                        updateActivityTitle(
                            activitiesViewModel = activitiesViewModel,
                            activity = selectedActivity!!,
                            name = it
                        )
                    }
                },

                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                )
            ) {
                Text("Save")
            }
        }
    }
}

private fun updateActivityTitle(
    activitiesViewModel: ActivitiesViewModel,
    activity: Activity,
    name: String
) {
    activitiesViewModel.updateActivityName(activityId = activity.activityId, name = name)
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