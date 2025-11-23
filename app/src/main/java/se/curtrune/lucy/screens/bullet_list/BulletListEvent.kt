package se.curtrune.lucy.screens.bullet_list

import se.curtrune.lucy.classes.item.Item

sealed interface BulletListEvent {
    data class OnClick(val item: Item): BulletListEvent
    data class Update(val item: Item): BulletListEvent
}