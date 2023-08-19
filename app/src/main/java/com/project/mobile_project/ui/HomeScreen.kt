package com.project.mobile_project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mobile_project.viewModel.ActivitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClicked:  () -> Unit,
    activitiesViewModel: ActivitiesViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        Column (modifier.padding(innerPadding)) {
            ActivitiesList(onItemClicked, activitiesViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesList(onItemClicked: () -> Unit, activitiesViewModel: ActivitiesViewModel) {
    val activities = activitiesViewModel.allActivities.collectAsState(initial = listOf()).value
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        content = {
            items(items = activities) { activity ->
                Card(
                    onClick =  {
                        activitiesViewModel.selectActivity(activity)
                        onItemClicked()
                    },
                    modifier = Modifier
                        .size(width = 150.dp, height = 150.dp)
                        .padding(8.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =  MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val scroll = rememberScrollState(0)

                        Text(
                            text = activity.name,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.verticalScroll(scroll)
                        )
                    }
                }
            }
        }
    )
}
