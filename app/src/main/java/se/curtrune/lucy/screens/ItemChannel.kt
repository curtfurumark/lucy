package se.curtrune.lucy.screens

import se.curtrune.lucy.classes.item.Item

sealed interface ItemChannel{
    data class Edit(val item: Item): ItemChannel
    data class ShowMessage(val message: String): ItemChannel
    data object  ShowAddItemDialog: ItemChannel
}
