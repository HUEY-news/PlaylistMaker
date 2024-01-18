package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.ui.search.model.SearchState
//import moxy.MvpView
//import moxy.viewstate.strategy.AddToEndSingleStrategy
//import moxy.viewstate.strategy.StateStrategyType

interface SearchView {

//    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: SearchState)
}