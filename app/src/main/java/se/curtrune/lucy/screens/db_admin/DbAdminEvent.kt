package se.curtrune.lucy.screens.db_admin

sealed interface DbAdminEvent {
    data class DBName(val name:String): DbAdminEvent
}