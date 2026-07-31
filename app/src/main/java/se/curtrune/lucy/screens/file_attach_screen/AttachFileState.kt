package se.curtrune.lucy.screens.file_attach_screen

import se.curtrune.lucy.classes.item.Item

data class AttachFileState(
    var item: Item,
    var heading: String = "file"
)