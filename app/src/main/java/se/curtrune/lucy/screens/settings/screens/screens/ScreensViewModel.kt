package se.curtrune.lucy.screens.settings.screens.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.app.LucindaApplication

class ScreensViewModel: ViewModel() {
    private val userSettings = LucindaApplication.appModule.userSettings
    private val _state = MutableStateFlow(ScreensState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            showAppointments = userSettings.showAppointmentsLink,
            showProjects = userSettings.showProjects,
            showMedicine = userSettings.showMedicine,
            showDuration = userSettings.showDuration,
            showDevScreen = userSettings.showDevScreen,
            showMentalStats = userSettings.showMentalStatsScreen,
            showTimeLine = userSettings.showTimeLine,
            showToDo = userSettings.showToDo,
        ) }
    }

    fun onEvent(event: ScreensEvent){
        when(event ){
            is ScreensEvent.ShowProjects -> {
                println("show projects ${event.visible}")
                setShowProjects(event.visible)
            }

            is ScreensEvent.ShowMedicine -> {
                setShowMedicine(event.visible)
            }

            is ScreensEvent.ShowDuration -> {
                setShowDuration(event.visible)

            }

            is ScreensEvent.ShowDevScreen -> {
                setShowDevScreen(event.visible)

            }

            is ScreensEvent.ShowMentalStats -> {
                setShowMentalStats(event.visible)
            }

            is ScreensEvent.ShowAppointments -> setShowAppointments(event.show)

            is ScreensEvent.ShowTimeLine ->{
                println("show timeline")
                setShowTimeLine(event.visible)
            }
            is ScreensEvent.ShowToDo ->{
                println("show to do")
                setShowToDo(event.visible)
            }
        }

    }

    private fun setShowAppointments(show: Boolean){
        userSettings.showAppointmentsLink = show
    }

    private fun setShowMentalStats(visible: Boolean) {
        userSettings.showMentalStatsScreen = visible
    }

    private fun setShowDevScreen(show: Boolean) {
        userSettings.showDevScreen = show
    }
    private fun setShowDuration(show: Boolean) {
        userSettings.showDuration = show
    }
    private fun setShowProjects(visible: Boolean){
        println("setShowProjects($visible)")
        userSettings.showProjects = visible
        _state.update { it.copy(
            showProjects = visible
        ) }
    }

    private fun setShowMedicine(show: Boolean) {
        userSettings.showMedicine = show
    }
    private fun setShowTimeLine(show: Boolean)  {
        userSettings.showTimeLine = show

    }
    private fun setShowToDo(show: Boolean) {
        userSettings.showToDo = show
    }



}