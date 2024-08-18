package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.runtime.saveable.Saver

sealed class ViewState (val title: String){
    data object CalenderViewState : ViewState("CalenderView")
    data object ListViewState : ViewState("ListView")
}

// Step 2: Create a custom Saver for Screen
val ViewStateSaver: Saver<ViewState, String> = Saver(
    save = { it.title },
    restore = { title ->
        when (title) {
            ViewState.CalenderViewState.title -> ViewState.CalenderViewState
            ViewState.ListViewState.title -> ViewState.ListViewState
            else -> throw IllegalArgumentException("Unknown screen title")
        }
    }
)