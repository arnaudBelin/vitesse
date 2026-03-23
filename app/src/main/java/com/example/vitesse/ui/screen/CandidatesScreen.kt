package com.example.vitesse.ui.screen

import android.R.id.message
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vitesse.data.entity.Candidate

@Composable
fun CandidatesScreen(
    candidates: List<Candidate> = emptyList()
) {
    LazyColumn(
    )
    {
        candidates.forEach { candidate ->
            item {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                ) {

                    if (candidate.pictureUrl.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(MaterialTheme.colorScheme.primary),

                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                // Default icon
                                imageVector = androidx.compose.material.icons.Icons.Default.Person,
                                contentDescription = "Default Candidate Icon",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(70.dp)
                            )
                        }
                    } else {
                        // display picture
                        AsyncImage(
                            model = candidate.pictureUrl,
                            contentDescription = "Candidate Picture",
                            modifier = Modifier
                                .size(70.dp)
                        )


                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Row() {
                            Text(
                                text = candidate.firstName,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = candidate.lastName,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row() {
                            candidate.note?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
