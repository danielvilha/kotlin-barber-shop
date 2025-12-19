package com.danielvilha.barbershop.features.home

import com.danielvilha.barbershop.features.util.SearchResult

sealed class HomeUiEvent {
    data class OnSearch(val search : String) : HomeUiEvent()
    data class OnQueryChange(val searchQuery : String) : HomeUiEvent()
    data class OnSearchTypeChange(val searchType : SearchType) : HomeUiEvent()
    data class OnItemSelected(val searchResult : SearchResult) : HomeUiEvent()
    data class OnBarbershopClick(val barbershopId : String) : HomeUiEvent()
    data object OnBackClick : HomeUiEvent()
    data object OnLogout : HomeUiEvent()
    data object RefreshUpcomingAppointment : HomeUiEvent()
}
