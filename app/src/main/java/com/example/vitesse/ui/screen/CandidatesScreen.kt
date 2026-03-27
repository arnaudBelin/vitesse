package com.example.vitesse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.ui.extension.displayFirstName
import com.example.vitesse.ui.extension.displayLastName
import com.example.vitesse.ui.navigation.Tab as CandidateTab
import java.util.Locale
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidatesScreen(
    modifier: Modifier = Modifier,
    candidates: List<Candidate> = emptyList(),
    selectedTab: CandidateTab,
    onTabSelected: (CandidateTab) -> Unit,
    onCandidateClick: (Int) -> Unit,
) {
    val displayedCandidates = when (selectedTab) {
        CandidateTab.ALL -> candidates
        CandidateTab.FAV -> candidates.filter { it.isFavorite }
    }

    Column(modifier = modifier) {
        PrimaryTabRow(selectedTabIndex = selectedTab.ordinal) {
            CandidateTab.entries.forEach { destination ->
                Tab(
                    selected = selectedTab == destination,
                    onClick = { onTabSelected(destination) },
                    text = {
                        Text(
                            text = stringResource(destination.label),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        CandidatesList(
            candidates = displayedCandidates,
            onCandidateClick = onCandidateClick
        )
    }
}

@Composable
fun CandidatesList(
    candidates: List<Candidate>,
    onCandidateClick: (Int) -> Unit,
) {
    LazyColumn {
        items(candidates, key = { candidate -> candidate.id }) { candidate ->
            Row(
                modifier = Modifier
                    .clickable { onCandidateClick(candidate.id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                if (candidate.pictureUrl.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Person,
                            contentDescription = "Default Candidate Icon",
                            tint = Color.White,
                            modifier = Modifier.size(70.dp)
                        )
                    }
                } else {
                    AsyncImage(
                        model = candidate.pictureUrl,
                        contentDescription = "Candidate Picture",
                        modifier = Modifier.size(70.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = candidate.displayFirstName(),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = candidate.displayLastName(),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    candidate.note?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
