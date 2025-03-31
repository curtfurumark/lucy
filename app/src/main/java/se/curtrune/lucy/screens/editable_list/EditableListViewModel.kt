package se.curtrune.lucy.screens.editable_list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.util.Logger


class EditableListViewModel : ViewModel() {
    private val repository = LucindaApplication.appModule.repository
    private var parent: Item? = null
    private val mutableItems = MutableLiveData<MutableList<Item>>()
    fun setParent(parent: Item?) {
        this.parent = parent
    }

    fun init(parent: Item) {
        Logger.log("EditableListViewModel.init(Item)", parent.heading)
        this.parent = parent
        mutableItems.value = ArrayList()
        mutableItems.value!!.add(createChild(parent))
        Logger.log("number of items", mutableItems.value!!.size)
    }

    fun addEmptyItem(index: Int) {
        Logger.log("...addEmptyItem(int)", index)
        mutableItems.value!!.add(index, createChild(parent!!))
    }

    val items: LiveData<MutableList<Item>>
        get() {
            Logger.log("EditableListViewModel.getItems()")
            return mutableItems
        }

    fun saveAll() {
        Logger.log("...saveAll()")
        val items: List<Item>? = mutableItems.value
        for (item in items!!) {
            if (!item.heading.isEmpty()) {
                parent?.let { repository.insertChild(it, item) }
            }
        }
    }


    fun setHeading(heading: String?, index: Int, context: Context?) {
        Logger.log("...setHeading(String, int, Context)", heading)
        mutableItems.value!![index].heading = heading
    }

    private fun createChild(parent: Item): Item {
        Logger.log("...createChild(Item)")
        val child = Item()
        child.parent = parent
        child.category = parent.category
        return child
    }
}
