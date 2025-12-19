
package com.danielvilha.barbershop.features.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.features.home.SearchType
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview() {
    BarberSearchBar(
        searchQuery = "",
        onQueryChange = {},
        onSearch = {},
        searchResults = emptyList(),
        onItemSelected = {},
        searchType = SearchType.BARBER,
        onBackClick = {}
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BarberSearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<SearchResult>,
    onItemSelected: (SearchResult) -> Unit,
    searchType: SearchType,
    onBackClick: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            expanded = expanded,
            onExpandedChange = { expanded = it },
            inputField = {
                val placeholderText = when (searchType) {
                    SearchType.BARBER -> stringResource(R.string.search_barber)
                    SearchType.LOCATION -> stringResource(R.string.search_location)
                }

                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = {
                        onQueryChange(it)
                        if (!expanded) expanded = true
                    },
                    onSearch = {
                        onSearch(searchQuery)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(text = placeholderText) },
                    leadingIcon = {
                        if (expanded) {
                            IconButton(onClick = {
                                onBackClick()
                                expanded = false
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                )
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
        ) {
            LazyColumn {
                items(searchResults) { result ->
                    val name = when (result) {
                        is SearchResult.Barber -> result.name
                        is SearchResult.Barbershop -> result.name
                    }
                    ListItem(
                        headlineContent = { Text(text = name) },
                        modifier = Modifier
                            .clickable {
                                onItemSelected(result)
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
