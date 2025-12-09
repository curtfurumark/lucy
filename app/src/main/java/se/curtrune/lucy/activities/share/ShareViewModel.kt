package se.curtrune.lucy.activities.share

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import se.curtrune.lucy.activities.share.composables.ShareEvent
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.screens.daycalendar.DayCalendarChannel

class ShareViewModel: ViewModel() {
    val repository = LucindaApplication.Companion.appModule.repository
    private val eventChannel = Channel<DayCalendarChannel>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun onEvent(event: ShareEvent){
        when(event){
            is ShareEvent.Save -> {
                println("save item: ${event.item}")
                repository.insert(event.item)
            }

            is ShareEvent.OnLinkClicked -> {
                println("link clicked: ${event.url}")
            }
        }
    }
}