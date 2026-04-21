package com.example.vitesse.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import com.example.vitesse.R
import com.example.vitesse.data.entity.Candidate
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    searchResults: List<Candidate>,
    onResultClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier.fillMaxWidth().semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier =
                Modifier.fillMaxWidth().align(Alignment.TopCenter).semantics {
                    traversalIndex = 0f
                },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = {
                        textFieldState.edit { replace(0, length, it) }
                        onQueryChange(it)
                    },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Display search results in a scrollable column
            Column(Modifier.verticalScroll(rememberScrollState())) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = {
                            Text(
                                "${result.firstName} ${
                            result.lastName.uppercase(
                                getDefault()
                            )
                        }"
                            )
                        },
                        modifier =
                            Modifier.clickable {
                                    onResultClick(result.id)
                                    // reset search query and collapse search bar
                                    textFieldState.edit { replace(0, length, "") }
                                    expanded = false
                                }
                                .fillMaxWidth(),
                    )
                }
            }
        }
    }
}
