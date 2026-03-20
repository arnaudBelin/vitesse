package com.example.vitesse.ui.screen

import android.R.id.message
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.vitesse.data.entity.Candidate

@Composable
fun CandidatesScreen(
    modifier: Modifier,
    candidates: List<Candidate> = emptyList()) {
    LazyColumn()
    {
        candidates.forEach { candidate ->
            item {
                Row() {
                    Column() {
                        if (candidate.picture.isNullOrEmpty()) {
                            Box(
                                modifier = modifier
                                    .background(MaterialTheme.colorScheme.background),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "Hello",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        } else {
                            // display picture

                        }


                    }
                    Column() {
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