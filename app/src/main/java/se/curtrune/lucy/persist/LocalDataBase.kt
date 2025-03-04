package se.curtrune.lucy.persist

import se.curtrune.lucy.activities.economy.classes.Transaction
import se.curtrune.lucy.classes.Item

interface LocalDataBase {
    fun delete(item: Item)
    fun getChildren(parent: Item): List<Item>
    fun getParent(item: Item): Item
    fun insert(item: Item)
    fun insert(items: List<Item>)
    fun insert(transaction: Transaction)
    fun insertChild(parent: Item, child: Item)
    fun selectItem(id: Long): Item
    fun selectItems(query: String): List<Item>
    fun touch(item: Item)
    fun update(item: Item)
}